package com.zbkj.admin.controller;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.LuckLotteryRecordResponse;
import com.zbkj.service.service.LuckLotteryRecordService;
import com.zbkj.service.service.LuckLotteryService;
import com.zbkj.service.service.LuckPrizeService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/admin/luck/luckLotteryRecord")
@Api(tags = "抽奖记录表") //配合swagger使用
public class LuckLotteryRecordController {

    @Autowired
    private LuckLotteryRecordService luckLotteryRecordService;

    @Autowired
    private LuckLotteryService luckLotteryService;

    @Autowired
    private LuckPrizeService luckPrizeService;

    @Autowired
    private UserService userService;

    /**
     * 查询中奖记录
     */
    @ApiOperation(value = "查询中奖记录")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<LuckLotteryRecordResponse>> prizeList(LuckLotteryRecord request, @Validated PageParamRequest pageRequest) {
        LambdaQueryWrapper<LuckLotteryRecord> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.orderByDesc(LuckLotteryRecord::getId);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<LuckLotteryRecord> list = luckLotteryRecordService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return CommonResult.success(CommonPage.restPage(new ArrayList<>()));
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

        CommonPage<LuckLotteryRecord> commonPage = CommonPage.restPage(list);
        CommonPage<LuckLotteryRecordResponse> restPage = CommonPage.restPage(responseList);

        BeanUtils.copyProperties(commonPage, restPage, "list");
        return CommonResult.success(restPage);
    }


    /**
     * 更新中奖记录
     */
    @ApiOperation(value = "更新中奖记录备注")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<Boolean> update(LuckLotteryRecord request) {
        if (request.getId() == null) {
            return CommonResult.failed("id参数必传");
        }
        LuckLotteryRecord record = luckLotteryRecordService.getById(request.getId());
        if (record == null) {
            return CommonResult.failed("中奖记录不存在");
        }
        record.setDeliverInfo(request.getDeliverInfo());
        record.setDeliverTime(System.currentTimeMillis());
        luckLotteryRecordService.updateById(record);

        return CommonResult.success(true);
    }

}
