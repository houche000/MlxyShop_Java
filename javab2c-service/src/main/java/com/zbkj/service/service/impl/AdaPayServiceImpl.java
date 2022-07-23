package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huifu.adapay.core.exception.BaseAdaPayException;
import com.huifu.adapay.model.Payment;
import com.huifu.adapay.model.Refund;
import com.zbkj.common.constants.AdaPayConstants;
import com.zbkj.common.constants.PayConstants;
import com.zbkj.common.constants.TaskConstants;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AdaPayServiceImpl implements AdaPayService {

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreCombinationService storeCombinationService;

    @Autowired
    private StorePinkService storePinkService;

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private RechargePayService rechargePayService;

    @Autowired
    private SystemConfigService systemConfigService;


    @Resource
    private RestTemplateUtil restTemplateUtil;


    @Override
    public Object queryPayResult(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            throw new CrmebException("订单编号不能为空");
        }

        Map<String, Object> payment = null;
        try {
            payment = Payment.query(orderNo);
        } catch (BaseAdaPayException e) {
            log.error("ada订单支付结果查询失败", e);
            return payment;
        }
        String status = payment.get("status").toString();
        if (!"succeeded".equals(status)) {
            return payment;
        }
        String orderId = payment.get("order_no").toString();
        String payPrice = payment.get("pay_amt").toString();
        // 切割字符串，判断是支付订单还是充值订单
        String pre = StrUtil.subPre(orderId, 5);
        if ("order".equals(pre)) {// 支付订单

            StoreOrder storeOrder = storeOrderService.getByOderId(orderId);
            if (ObjectUtil.isNull(storeOrder)) {
                throw new CrmebException("订单不存在");
            }
            if (storeOrder.getIsDel()) {
                throw new CrmebException("订单已被删除");
            }
            if (!storeOrder.getPayType().equals(PayConstants.PAY_TYPE_ADA_PAY)) {
                throw new CrmebException("不是ada支付类型订单，请重新选择支付方式");
            }

            if (storeOrder.getPaid()) {
                return Boolean.TRUE;
            }
            if (storeOrder.getPayPrice().compareTo(new BigDecimal(payPrice)) > 0) {
                return payment;
            }


            User user = userService.getById(storeOrder.getUid());
            if (ObjectUtil.isNull(user)) {
                throw new CrmebException("用户不存在");
            }

            Boolean updatePaid = transactionTemplate.execute(e -> {
                storeOrderService.updatePaid(orderId);
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
            redisUtil.lPush(TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER, orderId);
            return Boolean.TRUE;

        }

        // 充值订单
        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setOrderId(orderId);
        userRecharge = userRechargeService.getInfoByEntity(userRecharge);
        if (ObjectUtil.isNull(userRecharge)) {
            throw new CrmebException("没有找到订单信息");
        }
        if (userRecharge.getPaid()) {
            return Boolean.TRUE;
        }

        try {
            Boolean rechargePayAfter = rechargePayService.paySuccess(userRecharge);
            if (!rechargePayAfter) {
                throw new CrmebException("ada pay error : 数据保存失败==》" + orderId);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payment;

    }

    @Override
    public void refund(StoreOrderRefundRequest request, StoreOrder storeOrder) {

        Map<String, Object> refundParams = new HashMap<String, Object>(2);
        refundParams.put("refund_amt", request.getAmount().setScale(2, RoundingMode.HALF_UP).toString());
        refundParams.put("app_id", systemConfigService.getValueByKey(AdaPayConstants.ADA_APPID));
        refundParams.put("refund_order_no", request.getOrderNo());
        refundParams.put("notify_url", systemConfigService.getValueByKey(AdaPayConstants.ADA_CALLBACK_URL));
        try {
            Map<String, Object> refund = Refund.create(storeOrder.getOutTradeNo(), refundParams);

        } catch (BaseAdaPayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean queryRefund(String orderNo) {
        Map<String, Object> paymentParams = new HashMap<String, Object>(1);
        paymentParams.put("refund_id", orderNo);
        try {
            Map<String, Object> refund = Refund.query(paymentParams);

            return null;
        } catch (BaseAdaPayException e) {
            throw new RuntimeException(e);
        }
    }
}
