package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.combination.StorePink;
import com.zbkj.common.model.finance.UserExtract;
import com.zbkj.common.model.finance.UserRecharge;
import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.ExportUtil;
import com.zbkj.common.vo.*;
import com.zbkj.service.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
*  ExcelServiceImpl 接口实现
*  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
*/
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StoreBargainService storeBargainService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private StoreCombinationService storeCombinationService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private LuckLotteryRecordService luckLotteryRecordService;

    @Autowired
    private LuckLotteryService luckLotteryService;

    @Autowired
    private LuckPrizeService luckPrizeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private UserExtractServiceImpl userExtractService;

    @Autowired
    private StorePinkService storePinkService;

    @Autowired
    private CrmebConfig crmebConfig;

    /**
     * 导出砍价商品
     *
     * @param request 请求参数
     * @return 导出地址
     */
    @Override
    public String exportBargainProduct(StoreBargainSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);
        PageInfo<StoreBargainResponse> page = storeBargainService.getList(request, pageParamRequest);
        if (CollUtil.isEmpty(page.getList())) throw new CrmebException("没有可导出的数据!");
        List<StoreBargainResponse> list = page.getList();
        List<BargainProductExcelVo> voList = list.stream().map(temp -> {
            BargainProductExcelVo vo = new BargainProductExcelVo();
            BeanUtils.copyProperties(temp, vo);
            vo.setPrice("￥".concat(temp.getPrice().toString()));
            vo.setStatus(temp.getStatus() ? "开启" : "关闭");
            vo.setStartTime(temp.getStartTime());
            vo.setStopTime(temp.getStopTime());
            vo.setAddTime(temp.getAddTime());
            return vo;
        }).collect(Collectors.toList());

        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "砍价".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("title", "砍价活动名称");
        aliasMap.put("info", "砍价活动简介");
        aliasMap.put("price", "砍价金额");
        aliasMap.put("bargainNum", "用户每次砍价的次数");
        aliasMap.put("status", "砍价状态");
        aliasMap.put("startTime", "砍价开启时间");
        aliasMap.put("stopTime", "砍价结束时间");
        aliasMap.put("sales", "销量");
        aliasMap.put("quotaShow", "库存");
        aliasMap.put("giveIntegral", "返多少金豆");
        aliasMap.put("addTime", "添加时间");

        return ExportUtil.exportExecl(fileName, "砍价商品导出", voList, aliasMap);
    }

    /**
     * 导出拼团商品
     * @param request 请求参数
     * @return 导出地址
     */
    @Override
    public String exportCombinationProduct(StoreCombinationSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);
        PageInfo<StoreCombinationResponse> page = storeCombinationService.getList(request, pageParamRequest);
        if (CollUtil.isEmpty(page.getList())) throw new CrmebException("没有可导出的数据!");
        List<StoreCombinationResponse> list = page.getList();
        List<CombinationProductExcelVo> voList = list.stream().map(temp -> {
            CombinationProductExcelVo vo = new CombinationProductExcelVo();
            BeanUtils.copyProperties(temp, vo);
            vo.setIsShow(temp.getIsShow() ? "开启" : "关闭");
            vo.setStopTime(DateUtil.timestamp2DateStr(temp.getStopTime(), Constants.DATE_FORMAT_DATE));
            return vo;
        }).collect(Collectors.toList());

        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "拼团".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("id", "编号");
        aliasMap.put("title", "拼团名称");
        aliasMap.put("otPrice", "原价");
        aliasMap.put("price", "拼团价");
        aliasMap.put("quotaShow", "库存");
        aliasMap.put("countPeople", "拼团人数");
        aliasMap.put("countPeopleAll", "参与人数");
        aliasMap.put("countPeoplePink", "成团数量");
        aliasMap.put("sales", "销量");
        aliasMap.put("isShow", "商品状态");
        aliasMap.put("stopTime", "拼团结束时间");

        return ExportUtil.exportExecl(fileName, "拼团商品导出", voList, aliasMap);
    }

    /**
     * 商品导出
     * @param request 请求参数
     * @return 导出地址
     */
    @Override
    public String exportProduct(StoreProductSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);
        PageInfo<StoreProductResponse> storeProductResponsePageInfo = storeProductService.getAdminList(request, pageParamRequest);
        List<StoreProductResponse> list = storeProductResponsePageInfo.getList();
        if(list.size() < 1){
            throw new CrmebException("没有可导出的数据！");
        }

        //产品分类id
        List<String> cateIdListStr = list.stream().map(StoreProductResponse::getCateId).distinct().collect(Collectors.toList());

        HashMap<Integer, String> categoryNameList = new HashMap<Integer, String>();
        if(cateIdListStr.size() > 0){
            String join = StringUtils.join(cateIdListStr, ",");
            List<Integer> cateIdList = CrmebUtil.stringToArray(join);
            categoryNameList = categoryService.getListInId(cateIdList);
        }
        List<ProductExcelVo> voList = CollUtil.newArrayList();
        for (StoreProductResponse product : list ) {
            ProductExcelVo vo = new ProductExcelVo();
            vo.setStoreName(product.getStoreName());
            vo.setStoreInfo(product.getStoreInfo());
            vo.setCateName(CrmebUtil.getValueByIndex(categoryNameList, product.getCateId()));
            vo.setPrice("￥" + product.getPrice());
            vo.setStock(product.getStock().toString());
            vo.setSales(product.getSales().toString());
            vo.setBrowse(product.getBrowse().toString());
            voList.add(vo);
        }

        /**
         * ===============================
         * 以下为存储部分
         * ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "商品导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("storeName", "商品名称");
        aliasMap.put("storeInfo", "商品简介");
        aliasMap.put("cateName", "商品分类");
        aliasMap.put("price", "价格");
        aliasMap.put("stock", "库存");
        aliasMap.put("sales", "销量");
        aliasMap.put("browse", "浏览量");

        return ExportUtil.exportExecl(fileName, "商品导出", voList, aliasMap);
    }

    /**
     * 订单导出
     *
     * @param request  查询条件
     * @return 文件名称
     */
    @Override
    public String exportOrder(StoreOrderSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);

        request.setStatus(Constants.ORDER_STATUS_NOT_SHIPPED);
        CommonPage<StoreOrderDetailResponse> adminList = storeOrderService.getAdminList(request, pageParamRequest);
        List<StoreOrderDetailResponse> list = adminList.getList();
        if(list.size() < 1){
            throw new CrmebException("没有可导出的数据！");
        }

        List<OrderExcelVo> voList = CollUtil.newArrayList();
        for (StoreOrderDetailResponse order: list ) {
            OrderExcelVo vo = new OrderExcelVo();
            vo.setCreateTime(DateUtil.dateToStr(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            vo.setOrderId(order.getOrderId());
            vo.setOrderType(order.getOrderType());
            vo.setPayPrice(order.getPayPrice().toString());
            vo.setPayTypeStr(order.getPayTypeStr());
            vo.setProductName(order.getProductList().stream().map(item -> {
                OrderInfoDetailVo orderInfoDetailVo = item.getInfo();
                return orderInfoDetailVo.getProductName() + " | " + orderInfoDetailVo.getSku() + orderInfoDetailVo.getPrice() + "  *  " + orderInfoDetailVo.getPayNum();
            }).collect(Collectors.joining("\n")));
            vo.setRealName(order.getRealName());
            vo.setPhoneNumber(order.getUserPhone());

            String address = order.getUserAddress();
            vo.setAddress(address.replaceAll("北京市北京市东城区",""));
            vo.setStatusStr(order.getStatusStr().get("value"));
            voList.add(vo);
        }

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "订单导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("orderId", "订单号");
        aliasMap.put("payPrice", "实际支付金额");
//        aliasMap.put("payType", "支付方式");
        aliasMap.put("createTime", "创建时间");
//        aliasMap.put("status", "订单状态");
        aliasMap.put("productName", "商品信息");
        aliasMap.put("statusStr", "订单状态");
        aliasMap.put("payTypeStr", "支付方式");
//        aliasMap.put("isDel", "是否删除");
//        aliasMap.put("refundReasonWapImg", "退款图片");
//        aliasMap.put("refundReasonWapExplain", "退款用户说明");
//        aliasMap.put("refundReasonTime", "退款时间");
//        aliasMap.put("refundReasonWap", "前台退款原因");
//        aliasMap.put("refundReason", "不退款的理由");
//        aliasMap.put("refundPrice", "退款金额");
//        aliasMap.put("refundStatus", "退款状态状态，0 未退款 1 申请中 2 已退款");
//        aliasMap.put("verifyCode", "核销码");
        aliasMap.put("orderType", "订单类型");
//        aliasMap.put("remark", "订单管理员备注");
        aliasMap.put("realName", "收货人");
        aliasMap.put("phoneNumber", "电话");
        aliasMap.put("address", "收货地址");
        //        aliasMap.put("paid", "支付状态");
        //        aliasMap.put("type", "订单类型:0-普通订单，1-视频号订单");
        //        aliasMap.put("isAlterPrice", "是否改价,0-否，1-是");

        return ExportUtil.exportExecl(fileName, "订单导出", voList, aliasMap);

    }

    /**
     * 抽奖记录导出
     *
     * @param request 搜索条件
     */
    @Override
    public String exportLuckLottery(LuckLotteryRecord request) {
        LambdaQueryWrapper<LuckLotteryRecord> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.orderByDesc(LuckLotteryRecord::getId);
        List<LuckLotteryRecord> list = luckLotteryRecordService.list(queryWrapper);
        if (list.size() < 1) {
            throw new CrmebException("没有可导出的数据！");
        }
        Set<Integer> lotteryIds = list.stream().map(LuckLotteryRecord::getLotteryId).collect(Collectors.toSet());
        Set<Integer> prizeIds = list.stream().map(LuckLotteryRecord::getPrizeId).collect(Collectors.toSet());
        Set<Integer> userIds = list.stream().map(LuckLotteryRecord::getUid).collect(Collectors.toSet());
        List<LuckLottery> lotteryList = luckLotteryService.listByIds(lotteryIds);
        Map<Integer, LuckLottery> lotteryMap = lotteryList.stream().collect(Collectors.toMap(LuckLottery::getId, Function.identity(), (e1, e2) -> e1));
        List<LuckPrize> prizeList = luckPrizeService.listByIds(prizeIds);
        Map<Integer, LuckPrize> luckPrizeMap = prizeList.stream().collect(Collectors.toMap(LuckPrize::getId, Function.identity(), (e1, e2) -> e1));
        List<User> users = userService.listByIds(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getUid, Function.identity(), (e1, e2) -> e1));
        List<LuckLotteryRecordResponse> responseList = new ArrayList<>();
        for (LuckLotteryRecord record : list) {
            LuckLotteryRecordResponse response = new LuckLotteryRecordResponse();
            response.setLuckLottery(lotteryMap.get(record.getLotteryId()));
            response.setRecord(record);
            response.setLuckPrize(luckPrizeMap.get(record.getPrizeId()));
            response.setUser(userMap.get(record.getUid()));
            responseList.add(response);
        }

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "抽奖记录导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");
        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("id", "抽奖记录ID");
        aliasMap.put("uid", "用户UID");
        aliasMap.put("nickname", "用户昵称");
        aliasMap.put("lotteryId", "活动id");
        aliasMap.put("lotteryName", "活动名称");
        aliasMap.put("prizeId", "奖品id");
        aliasMap.put("prizeName", "奖品名称");
        aliasMap.put("isLuck", "是否中奖");
        aliasMap.put("addTime", "抽奖时间");
        aliasMap.put("isReceive", "是否领取");
        aliasMap.put("receiveTime", "领取时间");
        aliasMap.put("receiveInfo", "收获地址、备注等");
        aliasMap.put("isDeliver", "是否发货");
        aliasMap.put("deliverTime", "发货处理时间");
        aliasMap.put("deliverInfo", "发货单号、备注等");

        List<LuckLotteryExcelVo> voList = new ArrayList<>(responseList.size());
        for (LuckLotteryRecordResponse response : responseList) {
            LuckLotteryExcelVo excelVo = new LuckLotteryExcelVo();
            User user = response.getUser();
            if (user != null) {
                excelVo.setUid(user.getUid());
                excelVo.setNickname(user.getNickname());

            }
            LuckLottery luckLottery = response.getLuckLottery();
            if (luckLottery != null) {
                excelVo.setLotteryId(luckLottery.getId());
                excelVo.setLotteryName(luckLottery.getName());
            }
            LuckPrize luckPrize = response.getLuckPrize();
            if (luckPrize != null) {
                excelVo.setPrizeId(luckPrize.getId());
                excelVo.setPrizeName(luckPrize.getName());
            }
            excelVo.setId(response.getRecord().getId());
            excelVo.setIsLuck(response.getRecord().getType() == 1 ? "否" : "是");
            excelVo.setAddTime(DateUtil.dateToStr(new Date(response.getRecord().getAddTime()), "yyyy-MM-dd HH:mm:ss"));
            excelVo.setIsReceive(response.getRecord().getIsReceive() ? "是" : "否");
            excelVo.setReceiveTime(DateUtil.dateToStr(new Date(response.getRecord().getReceiveTime()), "yyyy-MM-dd HH:mm:ss"));
            excelVo.setDeliverInfo(response.getRecord().getDeliverInfo());
            excelVo.setIsDeliver(response.getRecord().getIsDeliver() == 1 ? "是" : "否");
            excelVo.setDeliverTime(DateUtil.dateToStr(new Date(response.getRecord().getDeliverTime()), "yyyy-MM-dd HH:mm:ss"));
            excelVo.setDeliverInfo(response.getRecord().getDeliverInfo());
            voList.add(excelVo);
        }

        return ExportUtil.exportExecl(fileName, "抽奖记录导出", voList, aliasMap);
    }


    /**
     * 用户充值导出
     *
     * @param request 搜索条件
     */
    @Override
    public String exportUserRecharge(UserRechargeSearchRequest request) {

        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
        //带 UserExtract 类的多条件查询
        LambdaQueryWrapper<UserRecharge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(request.getUid()) && request.getUid() > 0) {
            lambdaQueryWrapper.eq(UserRecharge::getUid, request.getUid());
        }
        if (StrUtil.isNotBlank(request.getKeywords())) {
            lambdaQueryWrapper.like(UserRecharge::getOrderId, request.getKeywords()); //订单号
        }
        //是否充值
        lambdaQueryWrapper.eq(UserRecharge::getPaid, true);

        //时间范围
        if (StrUtil.isNotBlank(dateLimit.getStartTime()) && StrUtil.isNotBlank(dateLimit.getEndTime())) {
            //判断时间
            int compareDateResult = DateUtil.compareDate(dateLimit.getEndTime(), dateLimit.getStartTime(), Constants.DATE_FORMAT);
            if (compareDateResult == -1) {
                throw new CrmebException("开始时间不能大于结束时间！");
            }

            lambdaQueryWrapper.between(UserRecharge::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        lambdaQueryWrapper.orderByDesc(UserRecharge::getId);
        List<UserRecharge> userRecharges = userRechargeService.list(lambdaQueryWrapper);

        if (CollUtil.isEmpty(userRecharges)) {
            throw new CrmebException("没有可导出的数据！");
        }

        List<Integer> userIds = userRecharges.stream().map(UserRecharge::getUid).collect(Collectors.toList());
        HashMap<Integer, User> userHashMap = userService.getMapListInUid(userIds);
        List<UserRechargeResponse> responseList = userRecharges.stream().map(e -> {
            User user = userHashMap.get(e.getUid());
            UserRechargeResponse r = new UserRechargeResponse();
            BeanUtils.copyProperties(e, r);
            if (null != user) {
                r.setAvatar(user.getAvatar());
                r.setNickname(user.getNickname());
            }
            return r;
        }).collect(Collectors.toList());

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "用户充值导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");
        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();

        aliasMap.put("id", "充值记录ID");
        aliasMap.put("uid", "充值用户UID");
        aliasMap.put("nickname", "充值用户昵称");
        aliasMap.put("price", "充值金额");
        aliasMap.put("givePrice", "购买赠送金额");
        aliasMap.put("rechargeType", "充值类型");
        aliasMap.put("payTime", "充值支付时间");
        aliasMap.put("createTime", "充值时间");
        aliasMap.put("refundPrice", "退款金额");

        return ExportUtil.exportExecl(fileName, "用户充值导出", responseList, aliasMap);
    }

    /**
     * 用户提现导出
     *
     * @param request 搜索条件
     */
    @Override
    public String exportUserExtract(UserExtractSearchRequest request) {
        //带 UserExtract 类的多条件查询
        LambdaQueryWrapper<UserExtract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(request.getKeywords())) {
            lambdaQueryWrapper.and(i -> i.
                    or().like(UserExtract::getWechat, request.getKeywords()).   //微信号
                            or().like(UserExtract::getRealName, request.getKeywords()). //名称
                            or().like(UserExtract::getBankCode, request.getKeywords()). //银行卡
                            or().like(UserExtract::getBankAddress, request.getKeywords()). //开户行
                            or().like(UserExtract::getAlipayCode, request.getKeywords()). //支付宝
                            or().like(UserExtract::getFailMsg, request.getKeywords()) //失败原因
            );
        }

        //提现状态
        if (request.getStatus() != null) {
            lambdaQueryWrapper.eq(UserExtract::getStatus, request.getStatus());
        }

        //提现资金类型
        if (request.getFundType() != null) {
            lambdaQueryWrapper.eq(UserExtract::getFundType, request.getFundType());
        }

        //提现方式
        if (!StringUtils.isBlank(request.getExtractType())) {
            lambdaQueryWrapper.eq(UserExtract::getExtractType, request.getExtractType());
        }

        //时间范围
        if (StringUtils.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lambdaQueryWrapper.between(UserExtract::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }

        //按创建时间降序排列
        lambdaQueryWrapper.orderByDesc(UserExtract::getCreateTime, UserExtract::getId);

        List<UserExtract> extractList = userExtractService.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(extractList)) {
            throw new CrmebException("没有可导出的数据！");
        }
        List<Integer> uidList = extractList.stream().map(UserExtract::getUid).distinct().collect(Collectors.toList());
        HashMap<Integer, User> userMap = userService.getMapListInUid(uidList);
        for (UserExtract userExtract : extractList) {
            userExtract.setNickName(Optional.ofNullable(userMap.get(userExtract.getUid()).getNickname()).orElse(""));
        }

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "用户提现导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");
        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();

        aliasMap.put("id", "提现记录ID");
        aliasMap.put("uid", "提现用户UID");
        aliasMap.put("nickname", "提现用户昵称");
        aliasMap.put("realName", "用户名称");
        aliasMap.put("fundType", "提现类型");
        aliasMap.put("extractType", "提现到的类型");
        aliasMap.put("bankCode", "银行卡");
        aliasMap.put("bankName", "银行名称");
        aliasMap.put("qrcodeUrl", "银行卡图片");
        aliasMap.put("bankAddress", "开户地址");
        aliasMap.put("alipayCode", "支付宝账号");
        aliasMap.put("extractPrice", "提现金额");
        aliasMap.put("mark", "备注");
        aliasMap.put("balance", "金额");
        aliasMap.put("failMsg", "无效原因");
        aliasMap.put("status", "提现状态");
        aliasMap.put("wechat", "微信号");
        aliasMap.put("createTime", "创建时间");
        aliasMap.put("updateTime", "更新时间");
        aliasMap.put("failTime", "失败时间");

        return ExportUtil.exportExecl(fileName, "用户提现导出", extractList, aliasMap);
    }

    /**
     * 用户列表导出
     *
     * @param request 搜索条件
     */
    @Override
    public String exportUser(UserSearchRequest request) {
        Map<String, Object> map = new HashMap<>();

        if (request.getIsPromoter() != null) {
            map.put("isPromoter", request.getIsPromoter() ? 1 : 0);
        }

        if (!StringUtils.isBlank(request.getGroupId())) {
            map.put("groupId", request.getGroupId());
        }

        if (!StringUtils.isBlank(request.getLabelId())) {
            String tagIdSql = CrmebUtil.getFindInSetSql("u.tag_id", request.getLabelId());
            map.put("tagIdSql", tagIdSql);
        }

        if (!StringUtils.isBlank(request.getLevel())) {
            map.put("level", request.getLevel());
        }

        if (StringUtils.isNotBlank(request.getUserType())) {
            map.put("userType", request.getUserType());
        }

        if (StringUtils.isNotBlank(request.getSex())) {
            map.put("sex", Integer.valueOf(request.getSex()));
        }

        if (StringUtils.isNotBlank(request.getCountry())) {
            map.put("country", request.getCountry());
            // 根据省市查询
            if (StrUtil.isNotBlank(request.getCity())) {
                request.setProvince(request.getProvince().replace("省", ""));
                request.setCity(request.getCity().replace("市", ""));
                map.put("addres", request.getProvince() + "," + request.getCity());
            }
        }

        if (StrUtil.isNotBlank(request.getPayCount())) {
            map.put("payCount", Integer.valueOf(request.getPayCount()));
        }

        if (request.getStatus() != null) {
            map.put("status", request.getStatus() ? 1 : 0);
        }

        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());

        if (!StringUtils.isBlank(dateLimit.getStartTime())) {
            map.put("startTime", dateLimit.getStartTime());
            map.put("endTime", dateLimit.getEndTime());
            map.put("accessType", request.getAccessType());
        }
        if (request.getKeywords() != null) {
            map.put("keywords", request.getKeywords());
        }
        List<User> userList = userService.findAdminList(map);

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "用户导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");
        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();

        aliasMap.put("uid", "用户UID");
        aliasMap.put("nickname", "用户昵称");
        aliasMap.put("realName", "真实姓名");
        aliasMap.put("account", "用户账号");
        // aliasMap.put("pwd", "用户密码");
        // aliasMap.put("birthday", "生日");
        // aliasMap.put("cardId", "身份证号码");
        // aliasMap.put("mark", "用户备注");
        // aliasMap.put("partnerId", "合伙人id");
        // aliasMap.put("groupId", "用户分组id");
        // aliasMap.put("tagId", "用户标签id");
        aliasMap.put("phone", "手机号码");
        // aliasMap.put("sex", "性别");
        // aliasMap.put("country", "国家");
        // aliasMap.put("addIp", "添加ip");
        // aliasMap.put("lastIp", "最后一次登录ip");
        aliasMap.put("nowMoney", "用户余额");
        aliasMap.put("brokeragePrice", "佣金金额");
        aliasMap.put("integral", "用户剩余金豆");
        // aliasMap.put("experience", "用户剩余经验");
        // aliasMap.put("signNumstatus", "用户状态");
        // aliasMap.put("spreadUid", "推广人id");
        // aliasMap.put("spreadTime", "推广员关联时间");
        // aliasMap.put("userType", "用户类型");
        // aliasMap.put("isPromoter", "是否为推广员");
        // aliasMap.put("payCount", "用户购买次数");
        // aliasMap.put("spreadCount", "下级人数");
        // aliasMap.put("addres", "详细地址");
        // aliasMap.put("adminid", "管理员编号");
        // aliasMap.put("loginType", "用户登陆类型");
        aliasMap.put("createTime", "创建时间");
        // aliasMap.put("updateTime", "更新时间");
        aliasMap.put("lastLoginTime", "最后一次登录时间");
        // aliasMap.put("path", "用户推广等级");
        // aliasMap.put("subscribe", "是否关注公众号");
        // aliasMap.put("promoterTime", "成为分销员时间");

        return ExportUtil.exportExecl(fileName, "用户导出", userList, aliasMap);
    }

    @Override
    public String exportPink(StorePinkSearchRequest request) {
        LambdaQueryWrapper<StorePink> lqw = Wrappers.lambdaQuery();
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
        List<StorePink> pinkList = storePinkService.list(lqw);
        if (CollUtil.isEmpty(pinkList)) {
            throw new CrmebException("没有可导出的数据！");
        }
        List<StorePinkExcelVo> excelVos = new ArrayList<>(pinkList.size());

        // 将拼团状态提换为订单状态
        for (StorePink pink : pinkList) {
            StorePinkExcelVo response = new StorePinkExcelVo();
            BeanUtils.copyProperties(pink, response);
            response.setCreateTime(DateUtil.dateToStr(new Date(pink.getAddTime()), "yyyy-MM-dd HH:mm:ss"));
            response.setStopTime(DateUtil.dateToStr(new Date(pink.getStopTime()), "yyyy-MM-dd HH:mm:ss"));
            if (pink.getIsSystem()) {
                response.setUserType("机器人");
            } else {
                response.setUserType("用户");
            }
            if (pink.getKId() == 0) {
                response.setUserRole("团长");
            } else {
                response.setUserRole("团员");
            }
            if (pink.getUid().equals(pink.getWinner())) {
                response.setIsWinner("是");
            } else {
                response.setIsWinner("否");
            }

            StoreOrder storeOrder = storeOrderService.getByOderId(pink.getOrderId());
            if (ObjectUtil.isNotNull(storeOrder)) {
                BeanUtils.copyProperties(storeOrder, response);
                response.setAddress(storeOrder.getUserAddress());
                response.setRealName(storeOrder.getRealName());
                response.setPhoneNumber(storeOrder.getUserPhone());
                List<StoreOrderInfo> orderInfos = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderId());
                if (CollUtil.isNotEmpty(orderInfos)) {
                    response.setProductInfo(orderInfos.stream().map(i -> i.getProductName() + " | " + i.getSku() + " * " + i.getPayNum()).collect(Collectors.joining("\n")));

                }
            }
            excelVos.add(response);
        }

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "拼团列表导出_".concat(DateUtil.nowDateTime(Constants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");
        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("id", "拼团ID");
        aliasMap.put("uid", "用户id");
        aliasMap.put("nickname", "用户昵称");
        aliasMap.put("orderId", "订单id");
        aliasMap.put("pid", "商品id");
        aliasMap.put("productInfo", "商品信息");
        aliasMap.put("totalPrice", "购买总金额");
        aliasMap.put("payType", "支付方式");
        aliasMap.put("userRole", "拼团角色");
        aliasMap.put("userType", "用户身份");
        aliasMap.put("isWinner", "是否中奖");
        aliasMap.put("ktIntegral", "开团金豆");
        aliasMap.put("incentiveCommission", "鼓励佣金");
        aliasMap.put("status", "拼团状态1进行中2已完成3未完成");
        aliasMap.put("orderStatus", "订单状态（0：待发货；1：待收货；2：已收货，待评价；3：已完成；）");
        aliasMap.put("refundStatus", "退款状态0 未退款 1 申请中 2 已退款 3退款中");
        aliasMap.put("createTime", "创建时间");
        aliasMap.put("stopTime", "结束时间");
        aliasMap.put("realName", "收货人");
        aliasMap.put("phoneNumber", "电话");
        aliasMap.put("address", "收货地址");

        return ExportUtil.exportExecl(fileName, "拼团列表导出", excelVos, aliasMap);
    }
}

