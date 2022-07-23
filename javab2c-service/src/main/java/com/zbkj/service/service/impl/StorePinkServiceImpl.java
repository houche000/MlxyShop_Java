package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.UserConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.combination.StorePink;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserToken;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreOrderRefundRequest;
import com.zbkj.common.request.StorePinkSearchRequest;
import com.zbkj.common.response.StorePinkAdminListResponse;
import com.zbkj.common.response.StorePinkDetailResponse;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.StorePinkDao;
import com.zbkj.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StorePinkService 实现类
 */
@Service
public class StorePinkServiceImpl extends ServiceImpl<StorePinkDao, StorePink> implements StorePinkService {

    @Resource
    private StorePinkDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private StoreCombinationService storeCombinationService;

    @Autowired
    private TemplateMessageService templateMessageService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private SystemNotificationService systemNotificationService;


    /**
    * 列表
    * @param request 请求参数
    * @param pageParamRequest 分页类参数
    * @author HZW
    * @since 2020-11-13
    * @return List<StorePink>
    */
    @Override
    public PageInfo<StorePinkAdminListResponse> getList(StorePinkSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StorePink> pinkPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyWord())) {
            lqw.and(qw -> qw.or().eq(StorePink::getUid, request.getKeyWord())
                    .or().eq(StorePink::getId, request.getKeyWord())
                    .or().like(StorePink::getNickname, request.getKeyWord()));
        }
        if (request.getIsSystem() != null) {
            lqw.eq(StorePink::getIsSystem, request.getIsSystem());
        }
        if (ObjectUtil.isNotNull(request.getStatus())) {
            lqw.eq(StorePink::getStatus, request.getStatus());
        }
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(StorePink::getAddTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        if (StrUtil.isNotBlank(request.getWinnerDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getWinnerDateLimit());
            lqw.between(StorePink::getWinnerTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        lqw.eq(StorePink::getKId, 0);
        lqw.orderByDesc(StorePink::getId);
        List<StorePink> storePinks = dao.selectList(lqw);
        if (CollUtil.isEmpty(storePinks)) {
            return CommonPage.copyPageInfo(pinkPage, CollUtil.newArrayList());
        }
        List<StorePinkAdminListResponse> list = storePinks.stream().map(pink -> {
            StorePinkAdminListResponse storePinkResponse = new StorePinkAdminListResponse();
            BeanUtils.copyProperties(pink, storePinkResponse);
            LambdaQueryWrapper<StorePink> countPeople = Wrappers.lambdaQuery();
            countPeople.eq(StorePink::getId, pink.getId()).or().eq(StorePink::getKId, pink.getId());
            storePinkResponse.setCountPeople(dao.selectCount(countPeople));
            storePinkResponse.setAutoSystem(pink.getIsSystem());
            storePinkResponse.setAddTime(DateUtil.timestamp2DateStr(pink.getAddTime(), Constants.DATE_FORMAT));
            storePinkResponse.setStopTime(DateUtil.timestamp2DateStr(pink.getStopTime(), Constants.DATE_FORMAT));
            StoreCombination combination = storeCombinationService.getById(pink.getCid());
            storePinkResponse.setTitle(combination.getTitle());
            storePinkResponse.setWinnerPeople(combination.getWinnerPeople());
            if (pink.getWinner() != null && !pink.getWinner().equals("")) {
                try {
                    // 拼团多人中奖cjh -----start
                    String[] uids = pink.getWinner().split(",");
                    List<User> winnerUsers = new ArrayList<>();
                    for(String winnerUid : uids){
                        winnerUsers.add(userService.getInfoByUid(Integer.valueOf(winnerUid)));
                    }
                    storePinkResponse.setWinnerUser(winnerUsers);
                    // 拼团多人中奖cjh -----end

                    // 拼团单人中奖cjh -----start
                    //storePinkResponse.setWinnerUser(pink.getWinner());
                    // 拼团单人中奖cjh -----end
                } catch (Exception ignored) {

                }
            }
            return storePinkResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pinkPage, list);
    }

    /**
     * 获取拼团列表cid
     * @param cid 拼团商品id
     * @return
     */
    @Override
    public List<StorePink> getListByCid(Integer cid) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getCid, cid);
        lqw.orderByDesc(StorePink::getId);
        return dao.selectList(lqw);
    }

    /**
     * 拼团实体查询
     * @param storePink
     * @return
     */
    @Override
    public List<StorePink> getByEntity(StorePink storePink) {
        LambdaQueryWrapper<StorePink> lqw = Wrappers.lambdaQuery();
        lqw.setEntity(storePink);
        return dao.selectList(lqw);
    }

    /**
     * PC拼团详情列表
     * @param pinkId 团长pinkId
     * @return
     */
    @Override
    public List<StorePinkDetailResponse> getAdminList(Integer pinkId) {
        LambdaQueryWrapper<StorePink> lqw = Wrappers.lambdaQuery();
        lqw.eq(StorePink::getId, pinkId).or().eq(StorePink::getKId, pinkId);
        lqw.orderByDesc(StorePink::getId);
        List<StorePink> pinkList = dao.selectList(lqw);
        // 将拼团状态提换为订单状态
        List<StorePinkDetailResponse> responseList = pinkList.stream().map(pink -> {
            StorePinkDetailResponse response = new StorePinkDetailResponse();
            BeanUtils.copyProperties(pink, response);
            StoreOrder storeOrder = storeOrderService.getByOderId(pink.getOrderId());
            if (ObjectUtil.isNotNull(storeOrder)) {
                List<StoreOrderInfo> orderInfos = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderId());
                response.setStoreOrderInfos(orderInfos);
                response.setStoreOrder(storeOrder);
                response.setCreateTime(storeOrder.getCreateTime().getTime());
                response.setOrderStatus(storeOrder.getStatus());
                response.setRefundStatus(storeOrder.getRefundStatus());
            }
            return response;
        }).collect(Collectors.toList());
        return responseList;
    }

    /**
     * PC拼团详情列表
     *
     * @return
     */
    @Override
    public PageInfo<StorePinkDetailResponse> getAdminList(StorePinkSearchRequest request, PageParamRequest pageParamRequest) {
        LambdaQueryWrapper<StorePink> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(request.getOrderNo()), StorePink::getOrderId, request.getOrderNo());
        lqw.like(StrUtil.isNotBlank(request.getKeyWord()), StorePink::getNickname, request.getKeyWord());
        lqw.eq(request.getId() != null, StorePink::getId, request.getId());
        lqw.eq(request.getCid() != null, StorePink::getCid, request.getCid());
        lqw.eq(request.getUid() != null, StorePink::getUid, request.getUid());
        lqw.eq(request.getIsSystem() != null, StorePink::getIsSystem, request.getIsSystem());
        lqw.eq(request.getStatus() != null, StorePink::getStatus, request.getStatus());
        //判断用户类型
        if (request.getUserType() != null) {
            if (request.getUserType() == 0) {
                lqw.eq(StorePink::getOrderId, "0");
            } else {
                lqw.ne(StorePink::getOrderId, "0");
            }
        }
        // 判断是否中奖
        if (request.getIsWinner() != null) {
            if (request.getIsWinner()) {
                lqw.inSql(StorePink::getId, "select id from eb_store_pink where winner = uid ");
            } else {
                lqw.inSql(StorePink::getId, "select id from eb_store_pink where winner != uid or winner IS NULL");
            }
        }
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(StorePink::getAddTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        if (StrUtil.isNotBlank(request.getEndDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getEndDateLimit());
            lqw.between(StorePink::getStopTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        if (StrUtil.isNotBlank(request.getWinnerDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getWinnerDateLimit());
            lqw.between(StorePink::getWinnerTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        lqw.orderByDesc(StorePink::getId);
        Page<StorePinkDetailResponse> pinkPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StorePink> pinkList = dao.selectList(lqw);
        if (CollUtil.isEmpty(pinkList)) {
            return CommonPage.copyPageInfo(pinkPage, CollUtil.newArrayList());
        }
        // 将拼团状态提换为订单状态
        List<StorePinkDetailResponse> responseList = pinkList.stream().map(pink -> {
            StorePinkDetailResponse response = new StorePinkDetailResponse();
            BeanUtils.copyProperties(pink, response);
            StoreOrder storeOrder = storeOrderService.getByOderId(pink.getOrderId());
            if (ObjectUtil.isNotNull(storeOrder)) {
                List<StoreOrderInfo> orderInfos = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderId());
                response.setStoreOrderInfos(orderInfos);
                response.setStoreOrder(storeOrder);
                response.setCreateTime(storeOrder.getCreateTime().getTime());
                response.setOrderStatus(storeOrder.getStatus());
                response.setRefundStatus(storeOrder.getRefundStatus());
            }
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pinkPage, responseList);
    }

    @Override
    public List<StorePink> getListByCidAndKid(Integer cid, Integer kid, Integer limit) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getCid, cid);
        lqw.eq(StorePink::getKId, kid);
        lqw.eq(StorePink::getIsRefund, false);
        lqw.orderByDesc(StorePink::getId);
        // 系统开团太多，只查询十个
        if (limit != null && kid == 0) {
            lqw.last("limit " + limit);
        }
        return dao.selectList(lqw);
    }

    @Override
    public Integer getCountByKid(Integer pinkId) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.select(StorePink::getId);
        lqw.eq(StorePink::getIsRefund, false);
        lqw.and(i -> i.eq(StorePink::getKId, pinkId).or().eq(StorePink::getId, pinkId));
        return dao.selectCount(lqw);
    }

    /**
     * 检查状态，更新数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void detectionStatus() {
        // 查找所有结束时间小等于当前的进行中拼团团长
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getStatus, 1);
        lqw.eq(StorePink::getKId, 0);
        lqw.le(StorePink::getStopTime, System.currentTimeMillis());
        List<StorePink> headList = dao.selectList(lqw);
        if (CollUtil.isEmpty(headList)) {
            return ;
        }
        /**
         * 1.判断是否拼团成功
         * 2.成功的修改状态
         * 3.失败的拼团改为失败，订单申请退款
         */
        List<StorePink> pinkSuccessList = CollUtil.newArrayList();
        List<StorePink> pinkFailList = CollUtil.newArrayList();
        List<StoreOrderRefundRequest> applyList = CollUtil.newArrayList();
        for (StorePink headPink : headList) {
            // 查询团员
            List<StorePink> memberList = getListByCidAndKid(headPink.getCid(), headPink.getId(), null);
            memberList.add(headPink);
            if (headPink.getPeople().equals(memberList.size())) {
                pinkSuccessList.add(headPink);
                continue;
            }
            // 计算虚拟比例，判断是否拼团成功
            // StoreCombination storeCombination = storeCombinationService.getById(headPink.getCid());
            // Integer virtual = storeCombination.getVirtualRation();// 虚拟成团比例
            // if (headPink.getPeople() <= memberList.size() + virtual) {
            //     // 可以虚拟成团
            //     pinkSuccessList.add(headPink);
            //     continue;
            // }
            // 失败
            headPink.setStatus(3);
            // 订单申请退款
            StoreOrderRefundRequest refundRequest = new StoreOrderRefundRequest();
            refundRequest.setOrderId(headPink.getId());
            refundRequest.setOrderNo(headPink.getOrderId());
            StoreOrder headOrder = storeOrderService.getById(headPink.getOrderIdKey());
            pinkFailList.add(headPink);
            if (headOrder != null) {
                refundRequest.setAmount(headOrder.getPayPrice().subtract(headOrder.getRefundPrice()));
                applyList.add(refundRequest);
            }

            // 团员处理
            if (CollUtil.isNotEmpty(memberList)) {
                memberList.forEach(i -> i.setStatus(3));
                List<StoreOrderRefundRequest> tempApplyList = memberList.stream().filter(i -> i.getOrderIdKey() > 0).map(i -> {
                    StoreOrderRefundRequest tempRefundRequest = new StoreOrderRefundRequest();
                    tempRefundRequest.setOrderId(i.getId());
                    tempRefundRequest.setOrderNo(i.getOrderId());
                    StoreOrder iOrder = storeOrderService.getById(i.getOrderIdKey());
                    tempRefundRequest.setAmount(iOrder.getPayPrice().subtract(iOrder.getRefundPrice()));
                    return tempRefundRequest;
                }).collect(Collectors.toList());
                pinkFailList.addAll(memberList);
                applyList.addAll(tempApplyList);
            }
        }
        if (CollUtil.isNotEmpty(pinkFailList) && pinkFailList.size() > 0) {
            boolean failUpdate = updateBatchById(pinkFailList);
            if (!failUpdate) throw new CrmebException("批量更新拼团状态，拼团未成功部分，失败");
        }
        if (applyList.size() > 0) {
            for (StoreOrderRefundRequest request : applyList) {
                try {
                    storeOrderService.refund(request);
                } catch (Exception e) {
                    log.error("拼团未成功,订单退款失败", e);
                }
            }
        }

        // 执行拼团成功后置处理，发放拼团奖励
        if (CollUtil.isNotEmpty(pinkSuccessList) && pinkSuccessList.size() > 0) {
            for (StorePink storePink : pinkSuccessList) {
                storePink.setStatus(2);
                orderPayService.pinkProcessingForPink(storePink);
            }
            this.updateBatchById(pinkSuccessList);
        }
    }

    /**
     * 发送消息通知
     * @param record 参数
     * @param user 拼团用户
     */
    private void pushMessageOrder(MyRecord record, User user, SystemNotification notification) {
        if (!record.getStr("payType").equals(Constants.PAY_TYPE_WE_CHAT)) {
            return ;
        }
        if (record.getInt("isChannel").equals(2)) {
            return ;
        }

        UserToken userToken;
        HashMap<String, String> temMap = new HashMap<>();
        // 公众号
        if (record.getInt("isChannel").equals(Constants.ORDER_PAY_CHANNEL_PUBLIC) && notification.getIsWechat().equals(1)) {
            userToken = userTokenService.getTokenByUserId(user.getUid(), UserConstants.USER_TOKEN_TYPE_WECHAT);
            if (ObjectUtil.isNull(userToken)) {
                return ;
            }
            // 发送微信模板消息
            temMap.put(Constants.WE_CHAT_TEMP_KEY_FIRST, "恭喜您拼团成功！我们将尽快为您发货。");
            temMap.put("keyword1", record.getStr("orderNo"));
            temMap.put("keyword2", record.getStr("proName"));
            temMap.put(Constants.WE_CHAT_TEMP_KEY_END, "感谢你的使用！");
            templateMessageService.pushTemplateMessage(notification.getWechatId(), temMap, userToken.getToken());
        } else if (notification.getIsRoutine().equals(1)) {
            // 小程序发送订阅消息
            userToken = userTokenService.getTokenByUserId(user.getUid(), UserConstants.USER_TOKEN_TYPE_ROUTINE);
            if (ObjectUtil.isNull(userToken)) {
                return ;
            }
            // 组装数据
//        temMap.put("character_string1",  record.getStr("orderNo"));
//        temMap.put("thing2", record.getStr("proName"));
//        temMap.put("thing5", "恭喜您拼团成功！我们将尽快为您发货。");
            temMap.put("character_string10",  record.getStr("orderNo"));
            temMap.put("thing7", record.getStr("proName"));
            temMap.put("thing9", "恭喜您拼团成功！我们将尽快为您发货。");
            templateMessageService.pushMiniTemplateMessage(notification.getRoutineId(), temMap, userToken.getToken());
        }
    }

    /**
     * 拼团成功
     * @param kid
     * @return
     */
    @Override
    public boolean pinkSuccess(Integer kid) {
        if (ObjectUtil.isNull(kid)) {
            return false;
        }
        StorePink teamPink = getById(kid);
        List<StorePink> memberList = getListByCidAndKid(teamPink.getCid(), kid, null);
        long timeMillis = System.currentTimeMillis();
        memberList.add(teamPink);
        memberList.forEach(i -> {
            i.setStatus(2);
            i.setStopTime(timeMillis);
        });
        return updateBatchById(memberList);
    }

    /**
     * 根据订单编号获取
     * @param orderId
     * @return
     */
    @Override
    public StorePink getByOrderId(String orderId) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getOrderId, orderId);
        return dao.selectOne(lqw);
    }

    /**
     * 获取最后3个拼团信息（不同用户）
     * @return List
     */
    @Override
    public List<StorePink> findSizePink(Integer size) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getIsRefund, false);
        lqw.in(StorePink::getStatus, 1, 2);
        lqw.groupBy(StorePink::getUid);
        lqw.orderByDesc(StorePink::getId);
        lqw.last(" limit " + size);
        return dao.selectList(lqw);
    }

    /**
     * 获取拼团参与总人数
     * @return Integer
     */
    @Override
    public Integer getTotalPeople() {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePink::getIsRefund, false);
        lqw.in(StorePink::getStatus, 1, 2);
        return dao.selectCount(lqw);
    }

    @Override
    public Boolean addBotToPink(Integer id) {
        StorePink pink = this.getById(id);
        if (ObjectUtil.isNull(pink) || pink.getIsRefund().equals(true) || pink.getStatus() == 3) {
            throw new CrmebException("拼团团长无效");
        }
        Integer headId;
        if (pink.getKId() == 0) {
            headId = pink.getId();
        } else {
            headId = pink.getKId();
        }

        StoreCombination combination = storeCombinationService.getById(pink.getCid());

        User user = userService.randomGetOneBot();

        Integer isExistence = isBotExistence(combination.getId(),headId,user.getUid());
        if(isExistence >= 1){
            throw new CrmebException("当前机器人已存在");
        }

        // 生成拼团表数据
        StorePink storePink = new StorePink();
        storePink.setUid(user.getUid());
        storePink.setIsSystem(true);
        storePink.setAvatar(user.getAvatar());
        storePink.setNickname(user.getNickname());
        storePink.setOrderId("0");
        storePink.setOrderIdKey(0);
        storePink.setTotalNum(1);
        storePink.setTotalPrice(BigDecimal.ZERO);
        storePink.setCid(combination.getId());
        storePink.setPid(combination.getProductId());
        storePink.setPeople(combination.getPeople());
        storePink.setPrice(combination.getPrice());
        Integer effectiveTime = combination.getEffectiveTime();// 有效小时数
        DateTime dateTime = cn.hutool.core.date.DateUtil.date();
        storePink.setAddTime(dateTime.getTime());
        DateTime hourTime = cn.hutool.core.date.DateUtil.offsetHour(dateTime, effectiveTime);
        long stopTime = hourTime.getTime();
        if (stopTime > combination.getStopTime()) {
            stopTime = combination.getStopTime();
        }
        storePink.setStopTime(stopTime);
        storePink.setKId(headId);
        storePink.setIsTpl(false);
        storePink.setIsRefund(false);
        storePink.setStatus(1);
        this.save(storePink);
        // 处理拼团支付成功
        orderPayService.pinkProcessingForPink(storePink);
        return true;
    }

    private Integer isBotExistence(Integer cid, Integer kid,Integer uid) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.select(StorePink::getId);
        lqw.eq(StorePink::getCid, cid);
        lqw.and(i -> i.eq(StorePink::getKId, kid).or().eq(StorePink::getId, kid));
        lqw.eq(StorePink::getUid, uid);
        lqw.eq(StorePink::getIsRefund, false);
        return dao.selectCount(lqw);
    }

    private Integer getCountByKidAndCid(Integer cid, Integer kid) {
        LambdaQueryWrapper<StorePink> lqw = new LambdaQueryWrapper<>();
        lqw.select(StorePink::getId);
        lqw.eq(StorePink::getCid, cid);
        lqw.and(i -> i.eq(StorePink::getKId, kid).or().eq(StorePink::getId, kid));
        lqw.eq(StorePink::getIsRefund, false);
        return dao.selectCount(lqw);
    }
}

