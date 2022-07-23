package com.zbkj.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zbkj.admin.model.ScheduleJob;
import com.zbkj.admin.service.ScheduleJobService;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.combination.StorePink;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreCombinationRequest;
import com.zbkj.common.request.StoreCombinationSearchRequest;
import com.zbkj.common.request.StorePinkSearchRequest;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.CategoryService;
import com.zbkj.service.service.StoreCombinationService;
import com.zbkj.service.service.StorePinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 拼团商品表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/store/combination")
@Api(tags = "商品——拼团——商品") //配合swagger使用
public class StoreCombinationController {

    @Autowired
    private StoreCombinationService storeCombinationService;

    @Autowired
    private StorePinkService storePinkService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 分页显示拼团商品表
     *
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     * @return
     */
    @PreAuthorize("hasAuthority('admin:combination:list')")
    @ApiOperation(value = "分页显示拼团商品表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StoreCombinationResponse>> getList(@Validated StoreCombinationSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        CommonPage<StoreCombinationResponse> commonPage = CommonPage.restPage(storeCombinationService.getList(request, pageParamRequest));
        return CommonResult.success(commonPage);
    }

    /**
     * 新增拼团商品表
     *
     * @param request 新增参数
     */
    @PreAuthorize("hasAuthority('admin:combination:save')")
    @ApiOperation(value = "新增拼团商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated StoreCombinationRequest request) {
        Integer id = storeCombinationService.saveCombination(request);
        if (id != null && id > 0) {
            if (StrUtil.isNotEmpty(request.getAutoSystemCron())) {
                // 添加定时任务

                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setCombinationId(id);
                scheduleJob.setBeanName("AutoStartPinkTask");
                scheduleJob.setMethodName("startBotPink");
                scheduleJob.setParams(id.toString());
                scheduleJob.setCronExpression(request.getAutoSystemCron());
                if (request.getAutoSystem() && request.getIsShow()) {
                    scheduleJob.setStatus(0);
                } else {
                    scheduleJob.setStatus(1);
                }
                scheduleJob.setCreateTime(new Date());
                scheduleJobService.updateScheduleJob(scheduleJob);
            }
            return CommonResult.success("新增拼团商品成功,添加定时任务成功");
        } else {
            return CommonResult.failed("新增拼团商品失败");
        }
    }

    /**
     * 删除拼团商品表
     *
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:combination:delete')")
    @ApiOperation(value = "删除拼团商品")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public CommonResult<String> delete(@RequestParam(value = "id") Integer id) {
        if (storeCombinationService.deleteById(id)) {
            ScheduleJob scheduleJob = scheduleJobService.getByCombinationId(id);
            if (scheduleJob != null) {
                scheduleJobService.deleteScheduleJob(scheduleJob);
            }
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改拼团商品表
     *
     * @param storeCombinationRequest 修改参数
     */
    @PreAuthorize("hasAuthority('admin:combination:update')")
    @ApiOperation(value = "修改拼团商品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated StoreCombinationRequest storeCombinationRequest) {
        if (storeCombinationService.updateCombination(storeCombinationRequest)) {
            if (StrUtil.isNotEmpty(storeCombinationRequest.getAutoSystemCron())) {
                // 添加定时任务
                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setCombinationId(storeCombinationRequest.getId());
                scheduleJob.setBeanName("AutoStartPinkTask");
                scheduleJob.setMethodName("startBotPink");
                scheduleJob.setParams(storeCombinationRequest.getId().toString());
                if (StrUtil.isNotEmpty(storeCombinationRequest.getAutoSystemCron())) {
                    scheduleJob.setCronExpression(storeCombinationRequest.getAutoSystemCron());
                    if (storeCombinationRequest.getAutoSystem() && storeCombinationRequest.getIsShow()) {
                        scheduleJob.setStatus(0);
                    } else {
                        scheduleJob.setStatus(1);
                    }
                    scheduleJob.setCreateTime(new Date());
                    ScheduleJob job = scheduleJobService.getByCombinationId(storeCombinationRequest.getId());
                    if (job != null) {
                        scheduleJob.setJobId(job.getJobId());
                    }
                    scheduleJobService.updateScheduleJob(scheduleJob);
                }
            }
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 查询拼团商品信息
     *
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:combination:info')")
    @ApiOperation(value = "拼团商品详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<StoreProductInfoResponse> info(@RequestParam(value = "id") Integer id) {
        StoreProductInfoResponse detail = storeCombinationService.getAdminDetail(id);
        return CommonResult.success(detail);
    }

    /**
     * cron校验
     * 返回最近十次的执行时间
     *
     * @param request
     */
    @PreAuthorize("hasAuthority('admin:combination:info')")
    @ApiOperation(value = "cron校验")
    @RequestMapping(value = "/validCron", method = RequestMethod.GET)
    public CommonResult<List<Date>> ValidCron(StoreCombinationRequest request) {
        try {
            CronExpression expression = new CronExpression(request.getAutoSystemCron());
            Date date;
            if (request.getStartTime() != null) {
                date = new Date(DateUtil.dateStr2Timestamp(request.getStartTime(), Constants.DATE_TIME_TYPE_BEGIN));
            } else {
                date = new Date();
            }
            List<Date> list = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                date = expression.getNextValidTimeAfter(date);
                list.add(i, date);
            }
            return CommonResult.success(list);
        } catch (ParseException e) {
            return CommonResult.failed("非法的cron表达式");
        }
    }

    /**
     * 修改拼团商品状态
     */
    @PreAuthorize("hasAuthority('admin:combination:update:status')")
    @ApiOperation(value = "修改拼团商品状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    public CommonResult<Object> updateStatus(@RequestParam(value = "id") Integer id, @RequestParam @Validated boolean isShow) {
        if (storeCombinationService.updateCombinationShow(id, isShow)) {
            StoreCombination combination = storeCombinationService.getById(id);
            if (StrUtil.isNotEmpty(combination.getAutoSystemCron())) {
                // 添加定时任务
                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setCombinationId(combination.getId());
                scheduleJob.setBeanName("AutoStartPinkTask");
                scheduleJob.setMethodName("startBotPink");
                scheduleJob.setParams(combination.getId().toString());
                scheduleJob.setCronExpression(combination.getAutoSystemCron());
                if (combination.getAutoSystem() && combination.getIsShow()) {
                    scheduleJob.setStatus(0);
                } else {
                    scheduleJob.setStatus(1);
                }
                scheduleJob.setCreateTime(new Date());
                ScheduleJob job = scheduleJobService.getByCombinationId(combination.getId());
                if (job != null) {
                    scheduleJob.setJobId(job.getJobId());
                }
                scheduleJobService.updateScheduleJob(scheduleJob);
            }
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 拼团统计
     */
    @PreAuthorize("hasAuthority('admin:combination:statistics')")
    @ApiOperation(value = "拼团统计")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public CommonResult<Map<String, Object>> statistics() {
        Map<String, Object> map = storeCombinationService.getAdminStatistics();
        return CommonResult.success(map);
    }

    /**
     * 拼团列表
     */
    @PreAuthorize("hasAuthority('admin:combination:combine:list')")
    @ApiOperation(value = "拼团列表")
    @RequestMapping(value = "/combine/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePinkAdminListResponse>> getCombineList(@Validated StorePinkSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        CommonPage<StorePinkAdminListResponse> responseCommonPage = CommonPage.restPage(storePinkService.getList(request, pageParamRequest));
        return CommonResult.success(responseCommonPage);
    }

    /**
     * 拼团订单列表
     */
    @PreAuthorize("hasAuthority('admin:combination:order:pink')")
    @ApiOperation(value = "拼团订单列表")
    @RequestMapping(value = "/order_pink", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePinkDetailResponse>> getPinkList(StorePinkSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        CommonPage<StorePinkDetailResponse> commonPage = CommonPage.restPage(storePinkService.getAdminList(request, pageParamRequest));
        return CommonResult.success(commonPage);
    }

    /**
     * 拼团订单列表
     */
    @PreAuthorize("hasAuthority('admin:combination:order:pink')")
    @ApiOperation(value = "拼团订单详情")
    @RequestMapping(value = "/pink/{id}", method = RequestMethod.GET)
    public CommonResult<StorePink> pink(@PathVariable("id") Integer id) {
        StorePink storePink = storePinkService.getById(id);
        return CommonResult.success(storePink);
    }

    /**
     * 拼团订单添加机器人
     */
    @PreAuthorize("hasAuthority('admin:combination:order:pink')")
    @ApiOperation(value = "拼团订单添加机器人")
    @RequestMapping(value = "/addBotToPink/{id}", method = RequestMethod.GET)
    public CommonResult<Boolean> addBotToPink(@PathVariable("id") Integer id) {
        Boolean isSuccess = storePinkService.addBotToPink(id);
        return CommonResult.success(isSuccess);
    }

    /**
     * 拼团订单列表
     */
    @PreAuthorize("hasAuthority('admin:combination:order:pink')")
    @ApiOperation(value = "拼团订单列表")
    @RequestMapping(value = "/order_pink/{id}", method = RequestMethod.GET)
    public CommonResult<List<StorePinkDetailResponse>> getPinkList(@PathVariable(value = "id") Integer id) {
        return CommonResult.success(storePinkService.getAdminList(id));
    }

    /**
     * 指定拼团中奖人员
     */
    @PreAuthorize("hasAuthority('admin:combination:order:designatedWinner')")
    @ApiOperation(value = "指定拼团中奖人员")
    @RequestMapping(value = "/designatedWinner", method = RequestMethod.GET)
    public CommonResult<Boolean> designatedWinner(Integer id, String winner) {
        StorePink storePink = storePinkService.getById(id);
        if (storePink.getKId() == 0) {
            storePink.setId(id);
            storePink.setWinner(winner);
            storePink.setWinnerTime(System.currentTimeMillis());
            storePinkService.updateById(storePink);
            LambdaUpdateWrapper<StorePink> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(StorePink::getKId, id);
            updateWrapper.set(StorePink::getWinner, winner);
            updateWrapper.set(StorePink::getWinnerTime, System.currentTimeMillis());
            storePinkService.update(updateWrapper);
        } else {
            StorePink pink = new StorePink();
            pink.setId(storePink.getKId());
            pink.setWinner(winner);
            pink.setWinnerTime(System.currentTimeMillis());
            storePinkService.updateById(storePink);
            LambdaUpdateWrapper<StorePink> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(StorePink::getKId, storePink.getId());
            updateWrapper.set(StorePink::getWinner, winner);
            updateWrapper.set(StorePink::getWinnerTime, System.currentTimeMillis());
            storePinkService.update(updateWrapper);
        }
        return CommonResult.success(true);
    }
}



