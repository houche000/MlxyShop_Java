package com.zbkj.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zbkj.admin.model.ScheduleJob;
import com.zbkj.admin.service.ScheduleJobService;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.model.presale.StorePresale;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.CategoryService;
import com.zbkj.service.service.StorePresaleService;
import com.zbkj.service.service.StorePresaleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * 预售商品表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/store/presale")
@Api(tags = "商品——预售——商品") //配合swagger使用
public class StorePresaleController {

    @Autowired
    private StorePresaleService storePresaleService;
    
    @Autowired
    private StorePresaleUserService storePinkService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 分页显示预售商品表
     *
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     * @return
     */
    @PreAuthorize("hasAuthority('admin:presale:list')")
    @ApiOperation(value = "分页显示预售商品表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePresaleResponse>> getList(@Validated StorePresaleSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        CommonPage<StorePresaleResponse> commonPage = CommonPage.restPage(storePresaleService.getList(request, pageParamRequest));
        return CommonResult.success(commonPage);
    }

    /**
     * 新增预售商品表
     *
     * @param request 新增参数
     */
    @PreAuthorize("hasAuthority('admin:presale:save')")
    @ApiOperation(value = "新增预售商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated StorePresaleRequest request) {
        Integer id = storePresaleService.saveCombination(request);
        if (id != null && id > 0) {
            return CommonResult.success("新增预售商品成功");
        } else {
            return CommonResult.failed("新增预售商品失败");
        }
    }

    /**
     * 删除预售商品表
     *
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:presale:delete')")
    @ApiOperation(value = "删除预售商品")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public CommonResult<String> delete(@RequestParam(value = "id") Integer id) {
        if (storePresaleService.deleteById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改预售商品表
     *
     * @param storeCombinationRequest 修改参数
     */
    @PreAuthorize("hasAuthority('admin:presale:update')")
    @ApiOperation(value = "修改预售商品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated StorePresaleRequest storeCombinationRequest) {
        if (storePresaleService.updateCombination(storeCombinationRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 查询预售商品信息
     *
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:presale:info')")
    @ApiOperation(value = "预售商品详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<StoreProductInfoResponse> info(@RequestParam(value = "id") Integer id) {
        StoreProductInfoResponse detail = storePresaleService.getAdminDetail(id);
        return CommonResult.success(detail);
    }

    /**
     * cron校验
     * 返回最近十次的执行时间
     *
     * @param request
     */
    @PreAuthorize("hasAuthority('admin:presale:info')")
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
     * 修改预售商品状态
     */
    @PreAuthorize("hasAuthority('admin:presale:update:status')")
    @ApiOperation(value = "修改预售商品状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    public CommonResult<Object> updateStatus(@RequestParam(value = "id") Integer id, @RequestParam @Validated boolean isShow) {
        if (storePresaleService.updateCombinationShow(id, isShow)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 预售统计
     */
    @PreAuthorize("hasAuthority('admin:presale:statistics')")
    @ApiOperation(value = "预售统计")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public CommonResult<Map<String, Object>> statistics() {
//        Map<String, Object> map = storePresaleService.getAdminStatistics();
        Map<String, Object> map = new HashMap<>();

        return CommonResult.success(map);
    }

    /**
     * 预售列表
     */
//    @PreAuthorize("hasAuthority('admin:presale:combine:list')")
//    @ApiOperation(value = "预售列表")
//    @RequestMapping(value = "/combine/list", method = RequestMethod.GET)
//    public CommonResult<CommonPage<StorePinkAdminListResponse>> getCombineList(@Validated StorePinkSearchRequest request, @Validated PageParamRequest pageParamRequest) {
//        CommonPage<StorePinkAdminListResponse> responseCommonPage = CommonPage.restPage(storePinkService.getList(request, pageParamRequest));
//        return CommonResult.success(null);
//    }

    /**
     * 预售订单列表
     */
    @PreAuthorize("hasAuthority('admin:presale:order:pink')")
    @ApiOperation(value = "预售订单列表")
    @RequestMapping(value = "/order_pink", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePinkDetailResponse>> getPinkList(StorePinkSearchRequest request, @Validated PageParamRequest pageParamRequest) {
//        CommonPage<StorePinkDetailResponse> commonPage = CommonPage.restPage(storePinkService.getAdminList(request, pageParamRequest));
        return CommonResult.success("");
    }

    /**
     * 预售订单列表
     */
    @PreAuthorize("hasAuthority('admin:presale:order:pink')")
    @ApiOperation(value = "预售订单详情")
    @RequestMapping(value = "/pink/{id}", method = RequestMethod.GET)
    public CommonResult<StorePresaleUser> pink(@PathVariable("id") Integer id) {
//        StorePresaleUser storePink = storePinkService.getById(id);
        StorePresaleUser storePink = new StorePresaleUser();
        return CommonResult.success(storePink);
    }

    /**
     * 预售订单添加机器人
     */
    @PreAuthorize("hasAuthority('admin:presale:order:pink')")
    @ApiOperation(value = "预售订单添加机器人")
    @RequestMapping(value = "/addBotToPink/{id}", method = RequestMethod.GET)
    public CommonResult<Boolean> addBotToPink(@PathVariable("id") Integer id) {
        Boolean isSuccess = storePinkService.addBotToPink(id);
        return CommonResult.success(isSuccess);
    }

    /**
     * 预售订单列表
     */
    @PreAuthorize("hasAuthority('admin:presale:order:pink')")
    @ApiOperation(value = "预售订单列表")
    @RequestMapping(value = "/order_pink/{id}", method = RequestMethod.GET)
    public CommonResult<List<StorePinkDetailResponse>> getPinkList(@PathVariable(value = "id") Integer id) {

//        List<StorePinkDetailResponse> list = storePinkService.getAdminList(id)
        List<StorePinkDetailResponse> list = new ArrayList<>();

        return CommonResult.success(list);
    }

    /**
     * 指定预售中奖人员
     */
    @PreAuthorize("hasAuthority('admin:presale:order:designatedWinner')")
    @ApiOperation(value = "指定预售中奖人员")
    @RequestMapping(value = "/designatedWinner", method = RequestMethod.GET)
    public CommonResult<Boolean> designatedWinner(Integer id, Integer winner) {
        StorePresaleUser storePink = storePinkService.getById(id);
//        if (storePink.getKId() == 0) {
//            storePink.setId(id);
//            storePink.setWinner(winner);
//            storePink.setWinnerTime(System.currentTimeMillis());
//            storePinkService.updateById(storePink);
//            LambdaUpdateWrapper<StorePresaleUser> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.eq(StorePresaleUser::getKId, id);
//            updateWrapper.set(StorePresaleUser::getWinner, winner);
//            updateWrapper.set(StorePresaleUser::getWinnerTime, System.currentTimeMillis());
//            storePinkService.update(updateWrapper);
//        } else {
//            StorePresaleUser pink = new StorePresaleUser();
//            pink.setId(storePink.getKId());
//            pink.setWinner(winner);
//            pink.setWinnerTime(System.currentTimeMillis());
//            storePinkService.updateById(storePink);
//            LambdaUpdateWrapper<StorePresaleUser> updateWrapper = new LambdaUpdateWrapper<>();
//            updateWrapper.eq(StorePresaleUser::getKId, storePink.getId());
//            updateWrapper.set(StorePresaleUser::getWinner, winner);
//            updateWrapper.set(StorePresaleUser::getWinnerTime, System.currentTimeMillis());
//            storePinkService.update(updateWrapper);
//        }
        return CommonResult.success(true);
    }
}



