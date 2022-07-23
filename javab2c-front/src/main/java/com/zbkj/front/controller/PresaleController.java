package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductRequest;
import com.zbkj.common.request.StorePinkRequest;
import com.zbkj.common.request.StorePinkSearchRequest;
import com.zbkj.common.response.*;
import com.zbkj.service.service.StoreCombinationService;
import com.zbkj.service.service.StorePresaleService;
import com.zbkj.service.service.StorePresaleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预售商品
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("api/front/presale")
@Api(tags = "预售商品")
public class PresaleController {

    @Autowired
    private StorePresaleService storeCombinationService;

    @Autowired
    private StorePresaleUserService storePresaleUserService;

    /**
     * 预售首页
     */
    @ApiOperation(value = "预售首页数据")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonResult<PresaleIndexResponse> index() {
        return CommonResult.success(storeCombinationService.getIndexInfo());
    }

    /**
     * 预售商品列表header
     */
    @ApiOperation(value = "预售商品列表header")
    @RequestMapping(value = "/header", method = RequestMethod.GET)
    public CommonResult<CombinationHeaderResponse> header() {
//        CombinationHeaderResponse a =  storeCombinationService.getHeader();
        CombinationHeaderResponse a =null;
        return CommonResult.success(a);
    }

    /**
     * 预售商品列表
     */
    @ApiOperation(value = "预售商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePresaleH5Response>> list(@Validated ProductRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeCombinationService.getH5List(request, pageParamRequest)));
    }

    /**
     * 预售商品详情
     */
    @ApiOperation(value = "预售商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResult<PresaleDetailResponse> detail(@PathVariable(value = "id") Integer id) {
        return CommonResult.success(storeCombinationService.getH5Detail(id));
    }

    /**
     * 预售商品预售列表
     */
//    @ApiOperation(value = "预售商品预售列表")
//    @RequestMapping(value = "/presale/list", method = RequestMethod.GET)
//    public CommonResult<CommonPage<StorePinkResponse>> combineList(@Validated StorePinkSearchRequest request, @Validated PageParamRequest pageParamRequest) {
//        CommonPage<StorePinkResponse> a =storeCombinationService.combineList(request, pageParamRequest);
//
//        return CommonResult.success(a);
//    }

    /**
     * 去预约
     *
     * @param id 预售商品id
     */
    @ApiOperation(value = "去预约")
    @RequestMapping(value = "/presale/{id}", method = RequestMethod.GET)
    public CommonResult<Object> goPresale(@PathVariable(value = "id") Integer id) {
        return CommonResult.success(storeCombinationService.goPresale(id));
    }

    /**
     * 获取预约商品信息
     *
     * @param id 预售商品id
     */
    @ApiOperation(value = "获取预约商品信息")
    @RequestMapping(value = "/getPresaleInfo/{id}", method = RequestMethod.GET)
    public CommonResult<StorePresaleUser> getPresaleInfo(@PathVariable(value = "id") Integer id) {
        List<StorePresaleUser> list = storePresaleUserService.getPresaleByPresaleId(id);
        return CommonResult.success(list.size() == 0 ? null : list.get(0));
    }


    /**
     * 更多预售
     */
    @ApiOperation(value = "更多预售")
    @RequestMapping(value = "/more", method = RequestMethod.GET)
    public CommonResult<PageInfo<StoreCombination>> getMore(@RequestParam Integer comId, @Validated PageParamRequest pageParamRequest) {
//        PageInfo<StoreCombination> more = storeCombinationService.getMore(pageParamRequest, comId);
        PageInfo<StoreCombination> more = null;
        return CommonResult.success(more);
    }



}
