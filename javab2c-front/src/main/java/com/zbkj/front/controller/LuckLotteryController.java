package com.zbkj.front.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.constants.IntegralRecordConstants;
import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserIntegralRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.LuckLotteryRecordResponse;
import com.zbkj.service.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController("LuckLotteryController")
@RequestMapping("api/front/luckLottery")
@Api(tags = "用户 -- 抽奖")
public class LuckLotteryController {

    @Autowired
    private UserService userService;

    @Autowired
    private LuckLotteryService luckLotteryService;

    @Autowired
    private LuckPrizeService luckPrizeService;

    @Autowired
    private LuckLotteryRecordService luckLotteryRecordService;

    @Autowired
    private UserIntegralRecordService userIntegralRecordService;


    /**
     * 查询抽奖活动
     */
    @ApiOperation(value = "查询抽奖活动")
    @RequestMapping(value = "/info/{lotteryId}", method = RequestMethod.POST)
    public CommonResult<LuckLottery> info(@PathVariable Integer lotteryId) {
        return CommonResult.success(luckLotteryService.getById(lotteryId));
    }

    /**
     * 查询中奖记录
     */
    @ApiOperation(value = "查询中奖记录")
    @RequestMapping(value = "/recordList", method = RequestMethod.POST)
    public CommonResult<CommonPage<LuckLotteryRecordResponse>> prizeList(LuckLotteryRecord request, @Validated PageParamRequest pageRequest) {
        LambdaQueryWrapper<LuckLotteryRecord> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.eq(LuckLotteryRecord::getUid, userService.getUserId());
        queryWrapper.orderByDesc(LuckLotteryRecord::getId);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<LuckLotteryRecord> list = luckLotteryRecordService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return CommonResult.success(CommonPage.restPage(new ArrayList<>()));
        }
        Set<Integer> lotteryIds = list.stream().map(LuckLotteryRecord::getLotteryId).collect(Collectors.toSet());
        Set<Integer> prizeIds = list.stream().map(LuckLotteryRecord::getPrizeId).collect(Collectors.toSet());
        List<LuckLottery> lotteryList = luckLotteryService.listByIds(lotteryIds);
        Map<Integer, LuckLottery> lotteryMap = lotteryList.stream().collect(Collectors.toMap(LuckLottery::getId, Function.identity(), (e1, e2) -> e1));
        List<LuckPrize> prizeList = luckPrizeService.listByIds(prizeIds);
        Map<Integer, LuckPrize> luckPrizeMap = prizeList.stream().collect(Collectors.toMap(LuckPrize::getId, Function.identity(), (e1, e2) -> e1));
        List<LuckLotteryRecordResponse> responseList = new ArrayList<>();
        for (LuckLotteryRecord record : list) {
            LuckLotteryRecordResponse response = new LuckLotteryRecordResponse();
            response.setLuckLottery(lotteryMap.get(record.getLotteryId()));
            response.setRecord(record);
            response.setLuckPrize(luckPrizeMap.get(record.getPrizeId()));
            responseList.add(response);
        }

        CommonPage<LuckLotteryRecordResponse> restPage = CommonPage.restPage(responseList);
        CommonPage<LuckLotteryRecord> commonPage = CommonPage.restPage(list);

        BeanUtils.copyProperties(commonPage, restPage, "list");
        return CommonResult.success(restPage);
    }

    /**
     * 领取奖品
     */
    @ApiOperation(value = "领取奖品")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonResult<Boolean> receive(LuckLotteryRecord record) {
        LambdaQueryWrapper<LuckLotteryRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LuckLotteryRecord::getId, record.getId());
        queryWrapper.eq(LuckLotteryRecord::getUid, userService.getUserId());
        LuckLotteryRecord record1 = luckLotteryRecordService.getOne(queryWrapper);
        if (record1 == null) {
            return CommonResult.failed("中奖记录不存在");
        }
        if (record1.getType() == 1) {
            return CommonResult.failed("未中奖");
        }
        record1.setIsReceive(true);
        record1.setReceiveInfo(record.getReceiveInfo());
        record1.setReceiveTime(System.currentTimeMillis());
        luckLotteryRecordService.updateById(record1);
        return CommonResult.success(true);
    }

    /**
     * 查询抽奖活动奖品列表
     */
    @ApiOperation(value = "查询抽奖活动奖品列表")
    @RequestMapping(value = "/prizeList/{lotteryId}", method = RequestMethod.POST)
    public CommonResult<List<LuckPrize>> prizeList(@PathVariable Integer lotteryId) {
        return CommonResult.success(luckPrizeService.list(new LambdaQueryWrapper<LuckPrize>().eq(LuckPrize::getLotteryId, lotteryId).eq(LuckPrize::getIsDel, false)));
    }


    /**
     * 开始抽奖
     */
    @ApiOperation(value = "开始抽奖")
    @RequestMapping(value = "/startLottery/{lotteryId}", method = RequestMethod.POST)
    public CommonResult<LuckLotteryRecordResponse> startLottery(@PathVariable Integer lotteryId) {
        LuckLottery lottery = luckLotteryService.getById(lotteryId);

        if (lottery == null) {
            return CommonResult.failed("抽奖活动不存在");
        }

        if (lottery.getStatus() == 0) {
            return CommonResult.failed("抽奖活动已结束");
        }
        User user = userService.getInfo();
        if (user.getIntegral() - lottery.getFactorNum() < 0) {
            return CommonResult.failed("金豆不足");
        }
        // 扣减金豆
        // 生成记录
        UserIntegralRecord integralRecord = new UserIntegralRecord();
        integralRecord.setUid(user.getUid());
        integralRecord.setLinkType(IntegralRecordConstants.INTEGRAL_RECORD_LINK_TYPE_LOTTERY);
        integralRecord.setTitle(IntegralRecordConstants.BROKERAGE_RECORD_TITLE_LUCK_LOTTERY);
        integralRecord.setIntegral(lottery.getFactorNum());
        integralRecord.setStatus(IntegralRecordConstants.INTEGRAL_RECORD_STATUS_COMPLETE);
        integralRecord.setType(IntegralRecordConstants.INTEGRAL_RECORD_TYPE_SUB);
        integralRecord.setBalance(user.getIntegral() - lottery.getFactorNum());
        integralRecord.setMark(StrUtil.format("参与抽奖活动{}扣减了{}金豆", lottery.getId() + lottery.getName(), lottery.getFactorNum()));
        userService.operationIntegral(user.getUid(), lottery.getFactorNum(), user.getIntegral(), "sub");
        userIntegralRecordService.save(integralRecord);

        // 开始抽奖操作
        // 生成一个1 到 10000 的随机数
        // 按奖品列表的权重开始，在权重区间内即中奖。
        Random random = new Random();
        int luckNumber = random.nextInt(10000) + 1;
        List<LuckPrize> prizeList = luckPrizeService.list(new LambdaQueryWrapper<LuckPrize>().eq(LuckPrize::getLotteryId, lotteryId).eq(LuckPrize::getIsDel, false));
        // 记录中奖奖品
        LuckPrize luckPrize = null;
        // 记录下当前奖品的权重区间上限
        int allNumber = 0;
        for (LuckPrize prize : prizeList) {
            allNumber += prize.getChance();
            // 如果当前奖品的权重区间上限大于幸运号码，则中奖
            if (luckNumber <= allNumber) {
                luckPrize = prize;
                break;
            }
        }
        if (luckPrize == null) {
            return CommonResult.failed("抽奖出现错误");
        }
        // 记录抽奖结果
        LuckLotteryRecord record = new LuckLotteryRecord();
        record.setLotteryId(lotteryId);
        record.setUid(user.getUid());
        record.setPrizeId(luckPrize.getId());
        record.setType(luckPrize.getType());
        record.setAddTime(System.currentTimeMillis());
        luckLotteryRecordService.save(record);
        LuckLotteryRecordResponse response = new LuckLotteryRecordResponse();
        response.setLuckLottery(lottery);
        response.setLuckPrize(luckPrize);
        response.setRecord(record);
        return CommonResult.success(response);
    }


}
