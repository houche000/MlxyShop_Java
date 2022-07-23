package com.zbkj.admin.controller;

import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * Excel导出 前端控制器
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("api/admin/export/excel")
@Api(tags = "导出 -- Excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    /**
     * 商品导出
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:product')")
    @ApiOperation(value = "产品")
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> export(@Validated StoreProductSearchRequest request) {
        String fileName = excelService.exportProduct(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 砍价商品导出
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:bargain')")
    @ApiOperation(value = "砍价商品导出")
    @RequestMapping(value = "/bargain/product", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportBargainProduct(@Validated StoreBargainSearchRequest request) {
        String fileName = excelService.exportBargainProduct(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 拼团商品导出
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:combiantion')")
    @ApiOperation(value = "拼团商品导出")
    @RequestMapping(value = "/combiantion/product", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportCombinationProduct(@Validated StoreCombinationSearchRequest request) {
        String fileName = excelService.exportCombinationProduct(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 订单导出
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:order')")
    @ApiOperation(value = "订单导出")
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportOrder(@Validated StoreOrderSearchRequest request) {
        String fileName = excelService.exportOrder(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 抽奖记录导出
     *
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:luckLottery')")
    @ApiOperation(value = "抽奖记录导出")
    @RequestMapping(value = "/luckLottery", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportLuckLottery(LuckLotteryRecord request) {
        String fileName = excelService.exportLuckLottery(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }


    /**
     * 用户充值导出
     *
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:userRecharge')")
    @ApiOperation(value = "用户充值导出")
    @RequestMapping(value = "/userRecharge", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportUserRecharge(@Validated UserRechargeSearchRequest request) {
        String fileName = excelService.exportUserRecharge(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 用户提现导出
     *
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:userExtract')")
    @ApiOperation(value = "用户提现导出")
    @RequestMapping(value = "/userExtract", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportUserExtract(@Validated UserExtractSearchRequest request) {
        String fileName = excelService.exportUserExtract(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 用户列表导出
     *
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:user')")
    @ApiOperation(value = "用户列表导出")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportUser(@Validated UserSearchRequest request) {
        String fileName = excelService.exportUser(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    /**
     * 拼团列表导出
     *
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('admin:export:excel:pink')")
    @ApiOperation(value = "拼团列表导出")
    @RequestMapping(value = "/pink", method = RequestMethod.GET)
    public CommonResult<HashMap<String, String>> exportPink(StorePinkSearchRequest request) {
        String fileName = excelService.exportPink(request);
        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

}



