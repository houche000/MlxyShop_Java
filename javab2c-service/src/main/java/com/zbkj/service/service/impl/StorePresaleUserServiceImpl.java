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
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserToken;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StorePresaleUserSearchRequest;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.StorePresaleUserDao;
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
 * StorePresaleUserService 实现类
 */
@Service
public class StorePresaleUserServiceImpl extends ServiceImpl<StorePresaleUserDao, StorePresaleUser> implements StorePresaleUserService {

    @Resource
    private StorePresaleUserDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private StorePresaleService storePresaleService;

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
    * @return List<StorePresaleUser>
    */
    @Override
    public PageInfo<StorePresaleUser> getList(StorePresaleUserSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StorePresaleUser> pinkPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyWord())) {
            lqw.and(qw -> qw.or().eq(StorePresaleUser::getUid, request.getKeyWord())
                    .or().like(StorePresaleUser::getNickname, request.getKeyWord()));
        }
        if (request.getIsSystem() != null) {
            lqw.eq(StorePresaleUser::getIsSystem, request.getIsSystem());
        }
        if (request.getIsThisUser() != null) {
            User user = userService.getInfo();
            lqw.eq(StorePresaleUser::getUid, user.getUid());
        }
        if (request.getIsWinner() != null) {
            lqw.eq(StorePresaleUser::getIsWinner, request.getIsWinner());
        }
        if (request.getPresaleId() != null) {
            lqw.eq(StorePresaleUser::getPresaleId, request.getPresaleId());
        }
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(StorePresaleUser::getAddTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }
        if (StrUtil.isNotBlank(request.getWinnerDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getWinnerDateLimit());
            lqw.between(StorePresaleUser::getWinnerTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
        }

        lqw.orderByDesc(StorePresaleUser::getAddTime);
        List<StorePresaleUser> storePinks = dao.selectList(lqw);
        return CommonPage.copyPageInfo(pinkPage, storePinks);
    }

    /**
     * 列表
     * @param request 请求参数
     * @param pageParamRequest 分页类参数
     * @author HZW
     * @since 2020-11-13
     * @return List<StorePresaleUser>
     */
    @Override
    public PageInfo<StorePresaleUser> getH5List(StorePresaleUserSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StorePresaleUser> pinkPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        if (request.getIsThisUser() != null) {
            User user = userService.getInfo();
            lqw.eq(StorePresaleUser::getUid, user.getUid());
        }
        if(request.getType() != null){
            if(request.getType() == 2){
                lqw.eq(StorePresaleUser::getIsWinner, 0);
            }
            if(request.getType() == 3){
                lqw.eq(StorePresaleUser::getIsWinner, 1);
            }
            if(request.getType() == 4){
                lqw.eq(StorePresaleUser::getIsWinner, 2);
            }
        }
        if (request.getPresaleId() != null) {
            lqw.eq(StorePresaleUser::getPresaleId, request.getPresaleId());
        }
        lqw.orderByDesc(StorePresaleUser::getAddTime);
        List<StorePresaleUser> storePinks = dao.selectList(lqw);
        return CommonPage.copyPageInfo(pinkPage, storePinks);
    }


    /**
     * 获取预售用户列表
     * @param preid 预售商品id
     * @return
     */
    @Override
    public List<StorePresaleUser> getListByCid(Integer preid) {
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getPresaleId, preid);
        lqw.orderByDesc(StorePresaleUser::getAddTime);
        return dao.selectList(lqw);
    }

    /**
     * 检测是否可以创建订单
     * @param presaleId
     * @return
     */
    @Override
    public Boolean isCreateOrder(Integer presaleId) {
        User user = userService.getInfo();
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getPresaleId, presaleId);
        lqw.eq(StorePresaleUser::getUid, user.getUid());
        lqw.eq(StorePresaleUser::getIsWinner,2);
        List<StorePresaleUser> list = dao.selectList(lqw);
        if(list.size() == 0){
            throw new CrmebException("您未中签");
        }
        StorePresaleUser storePresaleUser = list.get(0);
        if(storePresaleUser.getOrderId() != null & !storePresaleUser.getOrderId().equals("")){
            throw new CrmebException("您已下过单");
        }
        return true;
    }

    /**
     * 获取当前用户预约信息
     * @param presaleId 预售商品id
     * @return
     */
    @Override
    public List<StorePresaleUser> getPresaleByPresaleId(Integer presaleId) {
        User user = userService.getInfo();
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getPresaleId, presaleId);
        lqw.eq(StorePresaleUser::getUid, user.getUid());
        lqw.orderByDesc(StorePresaleUser::getAddTime);
        return dao.selectList(lqw);
    }

    /**
     * 根据中奖状态获取用户信息
     * @param presaleId 预售商品id
     * @return
     */
    @Override
    public List<StorePresaleUser> getUserByPresaleIdAndIsWinner(Integer presaleId,Integer isWinner) {
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getPresaleId, presaleId);
        lqw.eq(StorePresaleUser::getIsWinner,isWinner);
        lqw.orderByDesc(StorePresaleUser::getAddTime);
        return dao.selectList(lqw);
    }

    /**
     * 拼团实体查询
     * @param storePink
     * @return
     */
    @Override
    public List<StorePresaleUser> getByEntity(StorePresaleUser storePink) {
        LambdaQueryWrapper<StorePresaleUser> lqw = Wrappers.lambdaQuery();
        lqw.setEntity(storePink);
        return dao.selectList(lqw);
    }

    /**
     * 更新预约中签用户的订单号
     * @param presaleId
     * @return
     */
    @Override
    public Boolean updateOrderId(Integer presaleId,String orderId) {
        User user = userService.getInfo();
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getPresaleId, presaleId);
        lqw.eq(StorePresaleUser::getUid, user.getUid());
        lqw.eq(StorePresaleUser::getIsWinner,2);
        List<StorePresaleUser> list = dao.selectList(lqw);
        if(list.size() == 0){
            throw new CrmebException("您未中签");
        }
        StorePresaleUser storePresaleUser = list.get(0);
        storePresaleUser.setOrderId(orderId);
        updateById(storePresaleUser);
        return true;
    }

    /**
     * PC拼团详情列表
     * @param pinkId 团长pinkId
     * @return
     */
//    @Override
//    public List<StorePinkDetailResponse> getAdminList(Integer pinkId) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = Wrappers.lambdaQuery();
//        lqw.eq(StorePresaleUser::getId, pinkId).or().eq(StorePresaleUser::getKId, pinkId);
//        lqw.orderByDesc(StorePresaleUser::getId);
//        List<StorePresaleUser> pinkList = dao.selectList(lqw);
//        // 将拼团状态提换为订单状态
//        List<StorePinkDetailResponse> responseList = pinkList.stream().map(pink -> {
//            StorePinkDetailResponse response = new StorePinkDetailResponse();
//            BeanUtils.copyProperties(pink, response);
//            StoreOrder storeOrder = storeOrderService.getByOderId(pink.getOrderId());
//            if (ObjectUtil.isNotNull(storeOrder)) {
//                List<StoreOrderInfo> orderInfos = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderId());
//                response.setStoreOrderInfos(orderInfos);
//                response.setStoreOrder(storeOrder);
//                response.setCreateTime(storeOrder.getCreateTime().getTime());
//                response.setOrderStatus(storeOrder.getStatus());
//                response.setRefundStatus(storeOrder.getRefundStatus());
//            }
//            return response;
//        }).collect(Collectors.toList());
//        return responseList;
//    }

    /**
     * PC拼团详情列表
     *
     * @return
     */
//    @Override
//    public PageInfo<StorePinkDetailResponse> getAdminList(StorePinkSearchRequest request, PageParamRequest pageParamRequest) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = Wrappers.lambdaQuery();
//        lqw.eq(StrUtil.isNotBlank(request.getOrderNo()), StorePresaleUser::getOrderId, request.getOrderNo());
//        lqw.like(StrUtil.isNotBlank(request.getKeyWord()), StorePresaleUser::getNickname, request.getKeyWord());
//        lqw.eq(request.getId() != null, StorePresaleUser::getId, request.getId());
//        lqw.eq(request.getCid() != null, StorePresaleUser::getCid, request.getCid());
//        lqw.eq(request.getUid() != null, StorePresaleUser::getUid, request.getUid());
//        lqw.eq(request.getIsSystem() != null, StorePresaleUser::getIsSystem, request.getIsSystem());
//        lqw.eq(request.getStatus() != null, StorePresaleUser::getStatus, request.getStatus());
//        //判断用户类型
//        if (request.getUserType() != null) {
//            if (request.getUserType() == 0) {
//                lqw.eq(StorePresaleUser::getOrderId, "0");
//            } else {
//                lqw.ne(StorePresaleUser::getOrderId, "0");
//            }
//        }
//        // 判断是否中奖
//        if (request.getIsWinner() != null) {
//            if (request.getIsWinner()) {
//                lqw.inSql(StorePresaleUser::getId, "select id from eb_store_pink where winner = uid ");
//            } else {
//                lqw.inSql(StorePresaleUser::getId, "select id from eb_store_pink where winner != uid or winner IS NULL");
//            }
//        }
//        if (StrUtil.isNotBlank(request.getDateLimit())) {
//            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
//            lqw.between(StorePresaleUser::getAddTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
//        }
//        if (StrUtil.isNotBlank(request.getEndDateLimit())) {
//            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getEndDateLimit());
//            lqw.between(StorePresaleUser::getStopTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
//        }
//        if (StrUtil.isNotBlank(request.getWinnerDateLimit())) {
//            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getWinnerDateLimit());
//            lqw.between(StorePresaleUser::getWinnerTime, DateUtil.dateStr2Timestamp(dateLimit.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN), DateUtil.dateStr2Timestamp(dateLimit.getEndTime(), Constants.DATE_TIME_TYPE_END));
//        }
//        lqw.orderByDesc(StorePresaleUser::getId);
//        Page<StorePinkDetailResponse> pinkPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
//        List<StorePresaleUser> pinkList = dao.selectList(lqw);
//        if (CollUtil.isEmpty(pinkList)) {
//            return CommonPage.copyPageInfo(pinkPage, CollUtil.newArrayList());
//        }
//        // 将拼团状态提换为订单状态
//        List<StorePinkDetailResponse> responseList = pinkList.stream().map(pink -> {
//            StorePinkDetailResponse response = new StorePinkDetailResponse();
//            BeanUtils.copyProperties(pink, response);
//            StoreOrder storeOrder = storeOrderService.getByOderId(pink.getOrderId());
//            if (ObjectUtil.isNotNull(storeOrder)) {
//                List<StoreOrderInfo> orderInfos = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderId());
//                response.setStoreOrderInfos(orderInfos);
//                response.setStoreOrder(storeOrder);
//                response.setCreateTime(storeOrder.getCreateTime().getTime());
//                response.setOrderStatus(storeOrder.getStatus());
//                response.setRefundStatus(storeOrder.getRefundStatus());
//            }
//            return response;
//        }).collect(Collectors.toList());
//        return CommonPage.copyPageInfo(pinkPage, responseList);
//    }

//    @Override
//    public List<StorePresaleUser> getListByCidAndKid(Integer cid, Integer kid, Integer limit) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(StorePresaleUser::getCid, cid);
//        lqw.eq(StorePresaleUser::getKId, kid);
//        lqw.eq(StorePresaleUser::getIsRefund, false);
//        lqw.orderByDesc(StorePresaleUser::getId);
//        // 系统开团太多，只查询十个
//        if (limit != null && kid == 0) {
//            lqw.last("limit " + limit);
//        }
//        return dao.selectList(lqw);
//    }
//
//    @Override
//    public Integer getCountByKid(Integer pinkId) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
//        lqw.select(StorePresaleUser::getId);
//        lqw.eq(StorePresaleUser::getIsRefund, false);
//        lqw.and(i -> i.eq(StorePresaleUser::getKId, pinkId).or().eq(StorePresaleUser::getId, pinkId));
//        return dao.selectCount(lqw);
//    }

    /**
     * 检查状态，更新数据
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void detectionStatus() {
//        // 查找所有结束时间小等于当前的进行中拼团团长
//        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(StorePresaleUser::getStatus, 1);
//        lqw.eq(StorePresaleUser::getKId, 0);
//        lqw.le(StorePresaleUser::getStopTime, System.currentTimeMillis());
//        List<StorePresaleUser> headList = dao.selectList(lqw);
//        if (CollUtil.isEmpty(headList)) {
//            return ;
//        }
//        /**
//         * 1.判断是否拼团成功
//         * 2.成功的修改状态
//         * 3.失败的拼团改为失败，订单申请退款
//         */
//        List<StorePresaleUser> pinkSuccessList = CollUtil.newArrayList();
//        List<StorePresaleUser> pinkFailList = CollUtil.newArrayList();
//        List<StoreOrderRefundRequest> applyList = CollUtil.newArrayList();
//        for (StorePresaleUser headPink : headList) {
//            // 查询团员
//            List<StorePresaleUser> memberList = getListByCidAndKid(headPink.getCid(), headPink.getId(), null);
//            memberList.add(headPink);
//            if (headPink.getPeople().equals(memberList.size())) {
//                pinkSuccessList.add(headPink);
//                continue;
//            }
//            // 计算虚拟比例，判断是否拼团成功
//            // StoreCombination storeCombination = storeCombinationService.getById(headPink.getCid());
//            // Integer virtual = storeCombination.getVirtualRation();// 虚拟成团比例
//            // if (headPink.getPeople() <= memberList.size() + virtual) {
//            //     // 可以虚拟成团
//            //     pinkSuccessList.add(headPink);
//            //     continue;
//            // }
//            // 失败
//            headPink.setStatus(3);
//            // 订单申请退款
//            StoreOrderRefundRequest refundRequest = new StoreOrderRefundRequest();
//            refundRequest.setOrderId(headPink.getId());
//            refundRequest.setOrderNo(headPink.getOrderId());
//            StoreOrder headOrder = storeOrderService.getById(headPink.getOrderIdKey());
//            pinkFailList.add(headPink);
//            if (headOrder != null) {
//                refundRequest.setAmount(headOrder.getPayPrice().subtract(headOrder.getRefundPrice()));
//                applyList.add(refundRequest);
//            }
//
//            // 团员处理
//            if (CollUtil.isNotEmpty(memberList)) {
//                memberList.forEach(i -> i.setStatus(3));
//                List<StoreOrderRefundRequest> tempApplyList = memberList.stream().filter(i -> i.getOrderIdKey() > 0).map(i -> {
//                    StoreOrderRefundRequest tempRefundRequest = new StoreOrderRefundRequest();
//                    tempRefundRequest.setOrderId(i.getId());
//                    tempRefundRequest.setOrderNo(i.getOrderId());
//                    StoreOrder iOrder = storeOrderService.getById(i.getOrderIdKey());
//                    tempRefundRequest.setAmount(iOrder.getPayPrice().subtract(iOrder.getRefundPrice()));
//                    return tempRefundRequest;
//                }).collect(Collectors.toList());
//                pinkFailList.addAll(memberList);
//                applyList.addAll(tempApplyList);
//            }
//        }
//        if (CollUtil.isNotEmpty(pinkFailList) && pinkFailList.size() > 0) {
//            boolean failUpdate = updateBatchById(pinkFailList);
//            if (!failUpdate) throw new CrmebException("批量更新拼团状态，拼团未成功部分，失败");
//        }
//        if (applyList.size() > 0) {
//            for (StoreOrderRefundRequest request : applyList) {
//                try {
//                    storeOrderService.refund(request);
//                } catch (Exception e) {
//                    log.error("拼团未成功,订单退款失败", e);
//                }
//            }
//        }
//
//        // 执行拼团成功后置处理，发放拼团奖励
//        if (CollUtil.isNotEmpty(pinkSuccessList) && pinkSuccessList.size() > 0) {
//            for (StorePresaleUser storePink : pinkSuccessList) {
//                storePink.setStatus(2);
//                orderPayService.pinkProcessingForPink(storePink);
//            }
//            this.updateBatchById(pinkSuccessList);
//        }
//    }

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
//    @Override
//    public boolean pinkSuccess(Integer kid) {
//        if (ObjectUtil.isNull(kid)) {
//            return false;
//        }
//        StorePresaleUser teamPink = getById(kid);
//        List<StorePresaleUser> memberList = getListByCidAndKid(teamPink.getCid(), kid, null);
//        long timeMillis = System.currentTimeMillis();
//        memberList.add(teamPink);
//        memberList.forEach(i -> {
//            i.setStatus(2);
//            i.setStopTime(timeMillis);
//        });
//        return updateBatchById(memberList);
//    }

    /**
     * 根据订单编号获取
     * @param orderId
     * @return
     */
    @Override
    public StorePresaleUser getByOrderId(String orderId) {
        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StorePresaleUser::getOrderId, orderId);
        return dao.selectOne(lqw);
    }

    /**
     * 获取最后3个拼团信息（不同用户）
     * @return List
     */
//    @Override
//    public List<StorePresaleUser> findSizePink(Integer size) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(StorePresaleUser::getIsRefund, false);
//        lqw.in(StorePresaleUser::getStatus, 1, 2);
//        lqw.groupBy(StorePresaleUser::getUid);
//        lqw.orderByDesc(StorePresaleUser::getId);
//        lqw.last(" limit " + size);
//        return dao.selectList(lqw);
//    }


    @Override
    public Boolean addBotToPink(Integer id) {
        StorePresaleUser pink = this.getById(id);
//        StoreCombination combination = storeCombinationService.getById(pink.getCid());
        User user = userService.randomGetOneBot();

        // 生成拼团表数据
        StorePresaleUser storePink = new StorePresaleUser();
        storePink.setUid(user.getUid());
        storePink.setIsSystem(true);
        storePink.setAvatar(user.getAvatar());
        storePink.setNickname(user.getNickname());
        storePink.setOrderId("0");
        storePink.setOrderIdKey(0);
        storePink.setTotalNum(1);
        storePink.setTotalPrice(BigDecimal.ZERO);
//        storePink.setCid(combination.getId());
//        storePink.setPid(combination.getProductId());
//        storePink.setPeople(combination.getPeople());
//        storePink.setPrice(combination.getPrice());
//        Integer effectiveTime = combination.getEffectiveTime();// 有效小时数
        DateTime dateTime = cn.hutool.core.date.DateUtil.date();
        storePink.setAddTime(dateTime.getTime());
//        DateTime hourTime = cn.hutool.core.date.DateUtil.offsetHour(dateTime, effectiveTime);
//        long stopTime = hourTime.getTime();
//        if (stopTime > combination.getStopTime()) {
//            stopTime = combination.getStopTime();
//        }
//        storePink.setStopTime(stopTime);
//        storePink.setKId(headId);
        storePink.setIsTpl(false);
        storePink.setIsRefund(false);
//        storePink.setStatus(1);
        this.save(storePink);
        // 处理拼团支付成功
//        orderPayService.pinkProcessingForPink(storePink);
        return true;
    }

//    private Integer getCountByKidAndCid(Integer cid, Integer kid) {
//        LambdaQueryWrapper<StorePresaleUser> lqw = new LambdaQueryWrapper<>();
//        lqw.select(StorePresaleUser::getId);
//        lqw.eq(StorePresaleUser::getCid, cid);
//        lqw.and(i -> i.eq(StorePresaleUser::getKId, kid).or().eq(StorePresaleUser::getId, kid));
//        lqw.eq(StorePresaleUser::getIsRefund, false);
//        return dao.selectCount(lqw);
//    }
}

