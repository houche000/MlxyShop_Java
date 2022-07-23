package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.uipay.Uipay;
import cn.uipay.UipayClient;
import cn.uipay.exception.UipayException;
import cn.uipay.model.PayOrderQueryReqModel;
import cn.uipay.model.RefundOrderCreateReqModel;
import cn.uipay.model.RefundOrderQueryReqModel;
import cn.uipay.request.PayOrderQueryRequest;
import cn.uipay.request.RefundOrderCreateRequest;
import cn.uipay.request.RefundOrderQueryRequest;
import cn.uipay.response.PayOrderQueryResponse;
import cn.uipay.response.RefundOrderCreateResponse;
import cn.uipay.response.RefundOrderQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.PayConstants;
import com.zbkj.common.constants.TaskConstants;
import com.zbkj.common.constants.YyPayConfig;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.combination.StorePink;
import com.zbkj.common.model.finance.UserRecharge;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.user.User;
import com.zbkj.common.request.StoreOrderRefundRequest;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.utils.RestTemplateUtil;
import com.zbkj.service.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
public class YyPayServiceImpl implements YyPayService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private SystemConfigService systemConfigService;


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private RechargePayService rechargePayService;

    @Autowired
    private StoreCombinationService storeCombinationService;

    @Autowired
    private StorePinkService storePinkService;

    @Autowired
    private PayComponentOrderService componentOrderService;

    @Override
    public Boolean queryPayResult(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            throw new CrmebException("订单编号不能为空");
        }
        UipayClient uipayClient = new UipayClient();
        PayOrderQueryRequest request = new PayOrderQueryRequest();
        PayOrderQueryReqModel model = new PayOrderQueryReqModel();
        model.setMchNo(Uipay.mchNo);                                           // 商户号
        model.setAppId(Uipay.appId);
        model.setPayOrderId(orderNo);                            // 支付订单号
        request.setBizModel(model);

        try {
            PayOrderQueryResponse response = uipayClient.execute(request);
            log.info("验签结果：{}", response.checkSign(Uipay.apiKey));

            if (response.isSuccess(Uipay.apiKey)) {
                log.info("订单信息：{}", response);
                log.info("金额：{}", response.get().getAmount());
                if (response.get().getState() != 2) {
                    //未支付成功
                    return false;
                }
            }
        } catch (UipayException e) {
            e.printStackTrace();
        }

        // 切割字符串，判断是支付订单还是充值订单
        String pre = StrUtil.subPre(orderNo, 5);
        if ("order".equals(pre)) {// 支付订单

            StoreOrder storeOrder = storeOrderService.getByOderId(orderNo);
            if (ObjectUtil.isNull(storeOrder)) {
                throw new CrmebException("订单不存在");
            }
            if (storeOrder.getIsDel()) {
                throw new CrmebException("订单已被删除");
            }
            if (!storeOrder.getPayType().equals(PayConstants.PAY_TYPE_YY_PAY)) {
                throw new CrmebException("不是盈富支付类型订单，请重新选择支付方式");
            }

            if (storeOrder.getPaid()) {
                return Boolean.TRUE;
            }


            User user = userService.getById(storeOrder.getUid());
            if (ObjectUtil.isNull(user)) {
                throw new CrmebException("用户不存在");
            }

            Boolean updatePaid = transactionTemplate.execute(e -> {
                storeOrderService.updatePaid(orderNo);
                storeOrder.setPaid(true);
                if (storeOrder.getUseIntegral() > 0) {
                    userService.updateIntegral(user, storeOrder.getUseIntegral(), "sub");
                }
                // 处理拼团
                if (storeOrder.getCombinationId() > 0) {
                    //判断是否已经生成拼团订单
                    StorePink orderPink = storePinkService.getByOrderId(storeOrder.getOrderId());
                    if (orderPink != null) {
                        return true;
                    }
                    // 判断拼团团长是否存在
                    StorePink headPink = new StorePink();
                    Integer pinkId = storeOrder.getPinkId();
                    if (pinkId > 0) {
                        headPink = storePinkService.getById(pinkId);
                        if (ObjectUtil.isNull(headPink) || headPink.getIsRefund().equals(true) || headPink.getStatus() == 3 || headPink.getKId() > 0) {
                            pinkId = 0;
                        }
                    }
                    StoreCombination storeCombination = storeCombinationService.getById(storeOrder.getCombinationId());
                    // 如果拼团人数已满，重新开团
                    if (pinkId > 0) {
                        Integer count = storePinkService.getCountByKid(pinkId);
                        if (count >= storeCombination.getPeople()) {
                            pinkId = 0;
                        }
                    }
                    if (pinkId > 0) {
                        // 防止重复拼团进入一个团
                        if (storeOrder.getUid().equals(headPink.getUid())) {
                            pinkId = 0;
                        }
                    }
                    LambdaQueryWrapper<StorePink> userCountWrapper = new LambdaQueryWrapper<>();
                    userCountWrapper.eq(StorePink::getCid, headPink.getCid());
                    userCountWrapper.eq(StorePink::getKId, headPink.getId());
                    userCountWrapper.eq(StorePink::getUid, user.getUid());
                    userCountWrapper.eq(StorePink::getIsRefund, false);
                    int count = storePinkService.count(userCountWrapper);
                    if (count > 0) {
                        pinkId = 0;
                    }
                    // 生成拼团表数据
                    StorePink storePink = new StorePink();
                    storePink.setUid(user.getUid());
                    storePink.setAvatar(user.getAvatar());
                    storePink.setNickname(user.getNickname());
                    storePink.setOrderId(storeOrder.getOrderId());
                    storePink.setOrderIdKey(storeOrder.getId());
                    storePink.setTotalNum(storeOrder.getTotalNum());
                    storePink.setTotalPrice(storeOrder.getTotalPrice());
                    storePink.setCid(storeCombination.getId());
                    storePink.setPid(storeCombination.getProductId());
                    storePink.setPeople(storeCombination.getPeople());
                    storePink.setPrice(storeCombination.getPrice());
                    Integer effectiveTime = storeCombination.getEffectiveTime();// 有效小时数
                    DateTime dateTime = cn.hutool.core.date.DateUtil.date();
                    storePink.setAddTime(dateTime.getTime());
                    if (pinkId > 0) {
                        storePink.setStopTime(headPink.getStopTime());
                    } else {
                        DateTime hourTime = cn.hutool.core.date.DateUtil.offsetHour(dateTime, effectiveTime);
                        long stopTime = hourTime.getTime();
                        if (stopTime > storeCombination.getStopTime()) {
                            stopTime = storeCombination.getStopTime();
                        }
                        storePink.setStopTime(stopTime);
                    }
                    storePink.setKId(pinkId);
                    storePink.setIsTpl(false);
                    storePink.setIsRefund(false);
                    storePink.setStatus(1);
                    storePinkService.save(storePink);
                    // 如果是开团，需要更新订单数据
                    storeOrder.setPinkId(storePink.getId());
                    storeOrderService.updateById(storeOrder);
                }
                return Boolean.TRUE;
            });
            if (!updatePaid) {
                throw new CrmebException("支付成功更新订单失败");
            }
            // 添加支付成功task
            redisUtil.lPush(TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER, orderNo);
            return Boolean.TRUE;

        }

        // 充值订单
        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setOrderId(orderNo);
        userRecharge = userRechargeService.getInfoByEntity(userRecharge);
        if (ObjectUtil.isNull(userRecharge)) {
            throw new CrmebException("没有找到订单信息");
        }
        if (userRecharge.getPaid()) {
            return Boolean.TRUE;
        }
        Boolean rechargePayAfter = rechargePayService.paySuccess(userRecharge);
        if (!rechargePayAfter) {
            throw new CrmebException("wechat pay error : 数据保存失败==》" + orderNo);
        }
        return rechargePayAfter;
    }

    @Override
    public void refund(StoreOrderRefundRequest request, StoreOrder storeOrder) {

        //退款金额，必填
        long refundAmount = request.getAmount().multiply(ONE_HUNDRED).longValue();

        //获得初始化的YYPayClient
        String notifyUrl = systemConfigService.getValueByKey(YyPayConfig.REFUND_NOTIFY_URL);
        Uipay.setApiBase(YyPayConfig.URL);
        Uipay.mchNo = systemConfigService.getValueByKey(YyPayConfig.BUSINESS_NUMBER);
        Uipay.appId = systemConfigService.getValueByKey(YyPayConfig.APPID);
        Uipay.apiKey = systemConfigService.getValueByKey(YyPayConfig.APP_SECRET);
        UipayClient uipayClient = new UipayClient();
        RefundOrderCreateRequest refundOrderCreateRequest = new RefundOrderCreateRequest();
        RefundOrderCreateReqModel model = new RefundOrderCreateReqModel();
        model.setMchNo(Uipay.mchNo);                       // 商户号
        model.setAppId(Uipay.appId);                       // 应用ID
        model.setPayOrderId(storeOrder.getOutTradeNo());      // 支付订单号(与商户支付单号二者传一)
        //        model.setMchOrderNo("");                            // 商户支付单号(与支付订单号二者传一)
        model.setMchRefundNo(storeOrder.getOrderId());                // 商户退款单号
        model.setRefundAmount(refundAmount);                // 退款金额，单位分
        model.setCurrency(YyPayConfig.CURRENCY);           // 币种，目前只支持cny
        model.setRefundReason(storeOrder.getRefundReason());                    // 退款原因
        model.setNotifyUrl(notifyUrl);      // 异步通知地址
        //        model.setChannelExtra("");                          // 渠道扩展参数
        model.setExtParam("order");                              // 商户扩展参数,回调时原样返回
        refundOrderCreateRequest.setBizModel(model);
        try {
            RefundOrderCreateResponse response = uipayClient.execute(refundOrderCreateRequest);
            log.info("验签结果：{}", response.checkSign(Uipay.apiKey));
            // 判断退款发起是否成功（并不代表退款成功）
            if (response.isSuccess(Uipay.apiKey)) {
                String refundOrderId = response.get().getRefundOrderId();
                log.info("refundOrderId：{}", refundOrderId);
                log.info("mchRefundNo：{}", response.get().getMchRefundNo());
            } else {
                log.info("退款失败：refundOrderNo={}, msg={}", storeOrder.getOrderId(), response.getMsg());
                log.info("通道错误码：{}", response.get().getErrCode());
                log.info("通道错误信息：{}", response.get().getErrMsg());
            }
        } catch (UipayException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Boolean queryRefund(String orderNo) {

        StoreOrder storeOrder = storeOrderService.getByOderId(orderNo);
        if (ObjectUtil.isNull(storeOrder)) {
            throw new CrmebException("订单不存在");
        }
        if (storeOrder.getIsDel()) {
            throw new CrmebException("订单已被删除");
        }
        if (!storeOrder.getPayType().equals(PayConstants.PAY_TYPE_YY_PAY)) {
            throw new CrmebException("不是盈富支付类型订单");
        }
        if (!storeOrder.getPaid()) {
            throw new CrmebException("订单未支付");
        }
        if (!storeOrder.getRefundStatus().equals(3)) {
            throw new CrmebException("订单已不在退款中状态");
        }

        UipayClient uipayClient = new UipayClient();
        RefundOrderQueryRequest request = new RefundOrderQueryRequest();
        RefundOrderQueryReqModel model = new RefundOrderQueryReqModel();
        model.setMchNo(Uipay.mchNo);                                             // 商户号
        model.setAppId(Uipay.appId);                                             // 应用ID
        model.setRefundOrderId(orderNo);                         // 退款单号
        request.setBizModel(model);
        try {
            RefundOrderQueryResponse response = uipayClient.execute(request);
            log.info("验签结果：{}", response.checkSign(Uipay.apiKey));
            if (response.isSuccess(Uipay.apiKey)) {
                log.info("订单信息：{}", response);
                log.info("退款状态：{}", response.get().getState());
                log.info("退款金额：{}", response.get().getRefundAmount());
                if (2 == response.get().getState()) {
                    storeOrder.setRefundStatus(2);
                    boolean update = storeOrderService.updateById(storeOrder);
                    if (update) {
                        // 退款task
                        redisUtil.lPush(Constants.ORDER_TASK_REDIS_KEY_AFTER_REFUND_BY_USER, storeOrder.getId());
                    } else {
                        log.warn("盈富退款订单更新失败==>" + response);
                    }
                    return update;
                }
            }
        } catch (UipayException e) {
            e.printStackTrace();
        }
        return false;
    }
}
