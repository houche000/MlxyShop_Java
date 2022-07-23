package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.BrokerageRecordConstants;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.enums.EnumFundType;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.finance.UserExtract;
import com.zbkj.common.model.finance.UserExtractInfo;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserBill;
import com.zbkj.common.model.user.UserBrokerageRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserExtractRequest;
import com.zbkj.common.request.UserExtractSearchRequest;
import com.zbkj.common.response.BalanceResponse;
import com.zbkj.common.response.OcpayWithdrawResponse;
import com.zbkj.common.response.UserExtractRecordResponse;
import com.zbkj.common.response.UserExtractResponse;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.UserExtractDao;
import com.zbkj.service.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

/**
 * UserExtractServiceImpl 接口实现
 */
@Service
public class UserExtractServiceImpl extends ServiceImpl<UserExtractDao, UserExtract> implements UserExtractService {

    @Resource
    private UserExtractDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBillService billService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserBrokerageRecordService userBrokerageRecordService;

    @Autowired
    private OcpayService ocpayService;

    @Autowired
    private UserExtractInfoService userExtractInfoService;


    /**
     * 列表
     *
     * @param request          请求参数
     * @param pageParamRequest 分页类参数
     * @return List<UserExtract>
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    @Override
    public List<UserExtract> getList(UserExtractSearchRequest request, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        //带 UserExtract 类的多条件查询
        LambdaQueryWrapper<UserExtract> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(request.getKeywords())) {
            lambdaQueryWrapper.and(i -> i.
                    or().like(UserExtract::getWechat, request.getKeywords()).   //微信号
                            or().like(UserExtract::getRealName, request.getKeywords()). //名称
                            or().like(UserExtract::getUid, request.getKeywords()). //uId
                            or().like(UserExtract::getBankCode, request.getKeywords()). //银行卡
                            or().like(UserExtract::getBankAddress, request.getKeywords()). //开户行
                            or().like(UserExtract::getIcNumber, request.getKeywords()). //身份证
                            or().like(UserExtract::getAlipayCode, request.getKeywords()). //支付宝
                            or().like(UserExtract::getFailMsg, request.getKeywords()) //失败原因
            );
        }

        //用户Uid
        if (request.getUId() != null) {
            lambdaQueryWrapper.eq(UserExtract::getUid, request.getUId());
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

        List<UserExtract> extractList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(extractList)) {
            return extractList;
        }
        List<Integer> uidList = extractList.stream().map(o -> o.getUid()).distinct().collect(Collectors.toList());
        HashMap<Integer, User> userMap = userService.getMapListInUid(uidList);
        for (UserExtract userExtract : extractList) {
            userExtract.setNickName(Optional.ofNullable(userMap.get(userExtract.getUid()).getNickname()).orElse(""));

            if(StringUtils.isEmpty(userExtract.getIcNumber())){
                LambdaQueryWrapper<UserExtractInfo> userExInfo = new LambdaQueryWrapper<>();
                userExInfo.eq(UserExtractInfo::getUid,userExtract.getUid());
                List<UserExtractInfo> list =  userExtractInfoService.list(userExInfo);
                if (list.size() != 0) {
                    userExtract.setIcNumber(list.get(0).getIcNumber());
                }
            }

        }
        return extractList;
    }

    /**
     * 提现总金额
     * 总佣金 = 已提现佣金 + 未提现佣金
     * 已提现佣金 = 用户成功提现的金额
     * 未提现佣金 = 用户未提现的佣金 = 可提现佣金 + 冻结佣金 = 用户佣金
     * 可提现佣金 = 包括解冻佣金、提现未通过的佣金 = 用户佣金 - 冻结期佣金
     * 待提现佣金 = 待审核状态的佣金
     * 冻结佣金 = 用户在冻结期的佣金，不包括退回佣金
     * 退回佣金 = 因退款导致的冻结佣金退回
     */
    @Override
    public BalanceResponse getBalance(String dateLimit, Integer type) {
        String startTime = "";
        String endTime = "";
        if (StringUtils.isNotBlank(dateLimit)) {
            dateLimitUtilVo dateRage = DateUtil.getDateLimit(dateLimit);
            startTime = dateRage.getStartTime();
            endTime = dateRage.getEndTime();
        }

        // 已提现
        BigDecimal withdrawn = getWithdrawn(startTime, endTime, type);
        // 待提现(审核中)
        BigDecimal toBeWithdrawn = getWithdrawning(startTime, endTime, type);
        BigDecimal commissionTotal;
        if (EnumFundType.BALANCE.getCode().equals(type)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("sum(now_money) as blances");
            Map<String, Object> map = userService.getMap(queryWrapper);
            commissionTotal = new BigDecimal(map.get("blances").toString());
            return new BalanceResponse(withdrawn, commissionTotal.subtract(toBeWithdrawn), commissionTotal, toBeWithdrawn);
        } else {
            // 佣金总金额（单位时间）
            commissionTotal = userBrokerageRecordService.getTotalSpreadPriceBydateLimit(dateLimit);
            // 单位时间消耗的佣金
            BigDecimal subWithdarw = userBrokerageRecordService.getSubSpreadPriceByDateLimit(dateLimit);
            // 未提现
            BigDecimal unDrawn = commissionTotal.subtract(subWithdarw);
            return new BalanceResponse(withdrawn, unDrawn, commissionTotal, toBeWithdrawn);
        }
    }


    /**
     * 提现总金额
     *
     * @return BalanceResponse
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    @Override
    public BigDecimal getWithdrawn(String startTime, String endTime, Integer type) {
        return getSum(null, 1, type, startTime, endTime);
    }

    /**
     * 审核中总金额
     *
     * @return BalanceResponse
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    private BigDecimal getWithdrawning(String startTime, String endTime, Integer type) {
        return getSum(null, 0, type, startTime, endTime);
    }

    /**
     * 根据状态获取总额
     *
     * @return BigDecimal
     */
    private BigDecimal getSum(Integer userId, int status, Integer type, String startTime, String endTime) {
        LambdaQueryWrapper<UserExtract> lqw = Wrappers.lambdaQuery();
        if (null != userId) {
            lqw.eq(UserExtract::getUid, userId);
        }
        if (type != null) {
            lqw.eq(UserExtract::getFundType, type);
        }
        lqw.eq(UserExtract::getStatus, status);
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            lqw.between(UserExtract::getCreateTime, startTime, endTime);
        }
        List<UserExtract> userExtracts = dao.selectList(lqw);
        BigDecimal sum = ZERO;
        if (CollUtil.isNotEmpty(userExtracts)) {
            sum = userExtracts.stream().map(UserExtract::getExtractPrice).reduce(ZERO, BigDecimal::add);
        }
        return sum;
    }

    /**
     * 获取用户对应的提现数据
     *
     * @param userId 用户id
     * @return 提现数据
     */
    @Override
    public UserExtractResponse getUserExtractByUserId(Integer userId, Integer type) {
        QueryWrapper<UserExtract> qw = new QueryWrapper<>();
        qw.select("SUM(extract_price) as extract_price,count(id) as id, uid");
        qw.ge(type != null, "fund_type", type);
        qw.ge("status", 1);
        qw.eq("uid", userId);
        qw.groupBy("uid");
        UserExtract ux = dao.selectOne(qw);
        UserExtractResponse uexr = new UserExtractResponse();
        //        uexr.setEuid(ux.getUid());
        if (null != ux) {
            uexr.setExtractCountNum(ux.getId()); // 这里的id其实是数量，借变量传递
            uexr.setExtractCountPrice(ux.getExtractPrice());
        } else {
            uexr.setExtractCountNum(0); // 这里的id其实是数量，借变量传递
            uexr.setExtractCountPrice(ZERO);
        }

        return uexr;
    }

    /**
     * 提现审核
     *
     * @param id          提现申请id
     * @param status      审核状态 -1 未通过 0 审核中 1 已提现
     * @param backMessage 驳回原因
     * @return 审核结果
     */
    @Override
    public Boolean updateStatus(Integer id, Integer status, String backMessage) {
        if (status == -1 && StringUtils.isBlank(backMessage)) {
            throw new CrmebException("驳回时请填写驳回原因");
        }

        UserExtract userExtract = getById(id);
        if (ObjectUtil.isNull(userExtract)) {
            throw new CrmebException("提现申请记录不存在");
        }
        if (userExtract.getStatus() != 0) {
            throw new CrmebException("提现申请已处理过");
        }
        userExtract.setStatus(status);

        User user = userService.getById(userExtract.getUid());
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("提现用户数据异常");
        }

        Boolean execute = false;

        userExtract.setUpdateTime(cn.hutool.core.date.DateUtil.date());
        // 拒绝
        if (status == -1) {//未通过时恢复用户总金额
            userExtract.setFailMsg(backMessage);
            if (EnumFundType.BALANCE.getCode().equals(userExtract.getFundType())) {
                // 生成UserBill
                UserBill userBill = new UserBill();
                userBill.setUid(user.getUid());
                userBill.setTitle(BrokerageRecordConstants.BROKERAGE_RECORD_TITLE_WITHDRAW_FAIL);
                userBill.setPm(1);
                userBill.setCategory(Constants.USER_BILL_CATEGORY_MONEY);
                userBill.setNumber(userExtract.getExtractPrice());
                userBill.setStatus(1);
                userBill.setCreateTime(DateUtil.nowDateTime());
                userBill.setMark(StrUtil.format("提现申请拒绝返还余额{}", userExtract.getExtractPrice()));
                userBill.setBalance(user.getNowMoney().add(userExtract.getExtractPrice()));
                userBill.setCreateTime(new Date());
                userBill.setUpdateTime(new Date());
                execute = transactionTemplate.execute(e -> {
                    // 返还用户余额
                    userService.operationNowMoney(user.getUid(), userExtract.getExtractPrice(), user.getNowMoney(), "add");
                    updateById(userExtract);
                    userBill.setLinkId(userExtract.getId().toString());
                    billService.save(userBill);
                    return Boolean.TRUE;
                });
            } else if (EnumFundType.COMMISSION.getCode().equals(userExtract.getFundType())) {
                // 添加提现申请拒绝佣金记录
                UserBrokerageRecord brokerageRecord = new UserBrokerageRecord();
                brokerageRecord.setUid(user.getUid());
                brokerageRecord.setLinkId(userExtract.getId().toString());
                brokerageRecord.setLinkType(BrokerageRecordConstants.BROKERAGE_RECORD_LINK_TYPE_WITHDRAW);
                brokerageRecord.setType(BrokerageRecordConstants.BROKERAGE_RECORD_TYPE_ADD);
                brokerageRecord.setTitle(BrokerageRecordConstants.BROKERAGE_RECORD_TITLE_WITHDRAW_FAIL);
                brokerageRecord.setPrice(userExtract.getExtractPrice());
                brokerageRecord.setBalance(user.getBrokeragePrice().add(userExtract.getExtractPrice()));
                brokerageRecord.setMark(StrUtil.format("提现申请拒绝返还佣金{}", userExtract.getExtractPrice()));
                brokerageRecord.setStatus(BrokerageRecordConstants.BROKERAGE_RECORD_STATUS_COMPLETE);
                brokerageRecord.setCreateTime(DateUtil.nowDateTime());

                execute = transactionTemplate.execute(e -> {
                    // 返还佣金
                    userService.operationBrokerage(userExtract.getUid(), userExtract.getExtractPrice(), user.getBrokeragePrice(), "add");
                    updateById(userExtract);
                    userBrokerageRecordService.save(brokerageRecord);
                    return Boolean.TRUE;
                });
            }
        }

        // 同意
        if (status == 1) {
            //TODO TODO TODO,对接太平洋支付自己体现接口
            UserExtractInfo userExtractInfo = userExtractInfoService.getUserExtractInfoByUserId(userExtract.getUid());
            if (userExtractInfo==null){
                throw new CrmebException("用户提现账户信息不存在，提现失败");
            }
            try {
                OcpayWithdrawResponse withdraw = ocpayService.withdraw(userExtract.getId(),userExtract.getExtractPrice(), userExtractInfo.getBankName(), userExtractInfo.getRealName(), userExtractInfo.getAccountName());
                System.out.println(JSON.toJSONString(withdraw));
                if(!withdraw.getStatus()){
                    throw new CrmebException(withdraw.getReturnData().get("message").toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new CrmebException("连接支付平台提现失败");
            }

            // 获取佣金提现记录
            UserBrokerageRecord brokerageRecord = userBrokerageRecordService.getByLinkIdAndLinkType(userExtract.getId().toString(), BrokerageRecordConstants.BROKERAGE_RECORD_LINK_TYPE_WITHDRAW);
            if (EnumFundType.COMMISSION.getCode().equals(userExtract.getFundType()) && ObjectUtil.isNull(brokerageRecord)) {
                throw new CrmebException("对应的佣金记录不存在");
            }
            execute = transactionTemplate.execute(e -> {
                updateById(userExtract);
                if (EnumFundType.COMMISSION.getCode().equals(userExtract.getFundType())) {
                    brokerageRecord.setStatus(BrokerageRecordConstants.BROKERAGE_RECORD_STATUS_COMPLETE);
                    userBrokerageRecordService.updateById(brokerageRecord);
                }
                return Boolean.TRUE;
            });
        }
        return execute;
    }

    /**
     * 获取提现记录列表
     *
     * @param userId           用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<UserExtractRecordResponse> getExtractRecord(Integer userId, Integer type, PageParamRequest pageParamRequest) {
        Page<UserExtract> userExtractPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        QueryWrapper<UserExtract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userId);
        queryWrapper.eq(type != null, "fund_type", type);
        queryWrapper.groupBy("left(create_time, 7)");
        queryWrapper.orderByDesc("left(create_time, 7)");
        List<UserExtract> list = dao.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return new PageInfo<>();
        }
        ArrayList<UserExtractRecordResponse> userExtractRecordResponseList = CollectionUtil.newArrayList();
        for (UserExtract userExtract : list) {
            String date = DateUtil.dateToStr(userExtract.getCreateTime(), Constants.DATE_FORMAT_MONTH);
            userExtractRecordResponseList.add(new UserExtractRecordResponse(date, getListByMonth(userId, date)));
        }

        return CommonPage.copyPageInfo(userExtractPage, userExtractRecordResponseList);
    }

    private List<UserExtract> getListByMonth(Integer userId, String date) {
        QueryWrapper<UserExtract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userId);
        queryWrapper.apply(StrUtil.format(" left(create_time, 7) = '{}'", date));
        queryWrapper.orderByDesc("create_time");
        return dao.selectList(queryWrapper);
    }

    /**
     * 获取用户提现总金额
     *
     * @param userId 用户uid
     * @return BigDecimal
     */
    @Override
    public BigDecimal getExtractTotalMoney(Integer userId, Integer type) {
        return getSum(userId, 1, type, null, null);
    }


    /**
     * 提现申请
     *
     * @return Boolean
     */
    @Override
    public Boolean extractApply(UserExtractRequest request) {
        // 提现时间，超过时间不能提现
        String withdrawTime = systemConfigService.getValueByKeyException(Constants.CONFIG_USER_WITHDRAW_TIME);
        String[] time = withdrawTime.split(",");
        if (!DateUtil.hourBetween(time)) {
            throw new CrmebException(StrUtil.format("提现时间为{}-{}", time[0], time[1]));
        }

        //添加判断，提现金额不能后台配置金额
        String value = systemConfigService.getValueByKeyException(Constants.CONFIG_EXTRACT_MIN_PRICE);
        BigDecimal ten = new BigDecimal(value);
        if (request.getExtractPrice().compareTo(ten) < 0) {
            throw new CrmebException(StrUtil.format("最低提现金额{}元", ten));
        }

        User user = userService.getInfo();
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("提现用户信息异常");
        }
        BigDecimal money;
        if (EnumFundType.BALANCE.getCode().equals(request.getFundType())) {
            money = user.getNowMoney();//可提现总金额
            if (money.compareTo(ZERO) < 1) {
                throw new CrmebException("您当前没有金额可以提现");
            }

            if (money.compareTo(request.getExtractPrice()) < 0) {
                throw new CrmebException("你当前最多可提现 " + money + "元");
            }
        } else {
            money = user.getBrokeragePrice();//可提现总金额
            if (money.compareTo(ZERO) < 1) {
                throw new CrmebException("您当前没有金额可以提现");
            }

            if (money.compareTo(request.getExtractPrice()) < 0) {
                throw new CrmebException("你当前最多可提现 " + money + "元");
            }
        }
        UserExtractInfo userExtractInfo = userExtractInfoService.getUserExtractInfoByUserId(user.getUid());
        if(ObjectUtil.isEmpty(userExtractInfo)){
            throw new CrmebException("未绑定银行卡");
        }

        UserExtract userExtract = new UserExtract();
        BeanUtils.copyProperties(request, userExtract);
        userExtract.setUid(user.getUid());
        userExtract.setBalance(money.subtract(request.getExtractPrice()));
        userExtract.setIcNumber(userExtractInfo.getIcNumber());
        //存入银行名称
        if (StrUtil.isNotBlank(userExtract.getQrcodeUrl())) {
            userExtract.setQrcodeUrl(systemAttachmentService.clearPrefix(userExtract.getQrcodeUrl()));
        }

        if (EnumFundType.BALANCE.getCode().equals(request.getFundType())) {
            // 生成UserBill
            UserBill userBill = new UserBill();
            userBill.setUid(user.getUid());
            userBill.setTitle("用户提现");
            userBill.setPm(0);
            userBill.setCategory(Constants.USER_BILL_CATEGORY_MONEY);
            userBill.setType(Constants.USER_BILL_TYPE_EXTRACT);
            userBill.setNumber(userExtract.getExtractPrice());
            userBill.setStatus(1);
            userBill.setCreateTime(DateUtil.nowDateTime());
            userBill.setBalance(money.subtract(userExtract.getExtractPrice()));
            userBill.setCreateTime(new Date());
            userBill.setUpdateTime(new Date());
            userBill.setMark(StrUtil.format("提现申请扣除余额{}", userExtract.getExtractPrice()));
            Boolean execute = transactionTemplate.execute(e -> {
                // 保存提现记录
                save(userExtract);
                // 修改用户余额
                userService.operationNowMoney(user.getUid(), userExtract.getExtractPrice(), money, "sub");
                userBill.setLinkId(userExtract.getId().toString());
                billService.save(userBill);
                return Boolean.TRUE;
            });
            // 此处可添加提现申请通知

            return execute;
        } else if (EnumFundType.COMMISSION.getCode().equals(request.getFundType())) {
            // 添加佣金记录
            UserBrokerageRecord brokerageRecord = new UserBrokerageRecord();
            brokerageRecord.setUid(user.getUid());
            brokerageRecord.setLinkType(BrokerageRecordConstants.BROKERAGE_RECORD_LINK_TYPE_WITHDRAW);
            brokerageRecord.setType(BrokerageRecordConstants.BROKERAGE_RECORD_TYPE_SUB);
            brokerageRecord.setTitle(BrokerageRecordConstants.BROKERAGE_RECORD_TITLE_WITHDRAW_APPLY);
            brokerageRecord.setPrice(userExtract.getExtractPrice());
            brokerageRecord.setBalance(money.subtract(userExtract.getExtractPrice()));
            brokerageRecord.setMark(StrUtil.format("提现申请扣除佣金{}", userExtract.getExtractPrice()));
            brokerageRecord.setStatus(BrokerageRecordConstants.BROKERAGE_RECORD_STATUS_WITHDRAW);
            brokerageRecord.setCreateTime(DateUtil.nowDateTime());
            Boolean execute = transactionTemplate.execute(e -> {
                // 保存提现记录
                save(userExtract);
                // 修改用户佣金
                userService.operationBrokerage(user.getUid(), userExtract.getExtractPrice(), money, "sub");
                // 添加佣金记录
                brokerageRecord.setLinkId(userExtract.getId().toString());
                userBrokerageRecordService.save(brokerageRecord);
                return Boolean.TRUE;
            });
            // 此处可添加提现申请通知

            return execute;
        }

        return false;
    }

    /**
     * 修改提现申请
     *
     * @param id                 申请id
     * @param userExtractRequest 具体参数
     */
    @Override
    public Boolean updateExtract(Integer id, UserExtractRequest userExtractRequest) {
        UserExtract userExtract = new UserExtract();
        BeanUtils.copyProperties(userExtractRequest, userExtract);
        userExtract.setId(id);
        return updateById(userExtract);
    }

    /**
     * 提现申请待审核数量
     *
     * @return Integer
     */
    @Override
    public Integer getNotAuditNum(Integer type) {
        LambdaQueryWrapper<UserExtract> lqw = Wrappers.lambdaQuery();
        if (type != null) {
            lqw.eq(UserExtract::getFundType, type);
        }
        lqw.eq(UserExtract::getStatus, 0);
        return dao.selectCount(lqw);
    }

    @Override
    public Boolean changeExtractStatusById(Integer id, Integer status) {

        UserExtract userExtract = getById(id);
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx " + userExtract.toString());
        if(ObjectUtil.isEmpty(userExtract)){
            throw new CrmebException("订单不存在");
        }

        User user = userService.getById(userExtract.getUid());
        UserBill userBill = new UserBill();
        userBill.setUid(user.getUid());
        userBill.setTitle(BrokerageRecordConstants.BROKERAGE_RECORD_TITLE_WITHDRAW_FAIL);
        userBill.setPm(1);
        userBill.setCategory(Constants.USER_BILL_CATEGORY_MONEY);
        userBill.setNumber(userExtract.getExtractPrice());
        userBill.setStatus(1);
        userBill.setCreateTime(DateUtil.nowDateTime());
        userBill.setMark(StrUtil.format("提现申请拒绝返还余额{}", userExtract.getExtractPrice()));
        userBill.setBalance(user.getNowMoney().add(userExtract.getExtractPrice()));
        userBill.setCreateTime(new Date());
        userBill.setUpdateTime(new Date());
        return transactionTemplate.execute(e -> {
            // 返还用户余额
            userService.operationNowMoney(user.getUid(), userExtract.getExtractPrice(), user.getNowMoney(), "add");
            userExtract.setStatus(status);
            userExtract.setUpdateTime(new Date());
            updateById(userExtract);
            userBill.setLinkId(userExtract.getId().toString());
            billService.save(userBill);
            return Boolean.TRUE;
        });
    }
}

