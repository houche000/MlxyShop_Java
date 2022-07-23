package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductRequest;
import com.zbkj.common.request.StorePresaleUserSearchRequest;
import com.zbkj.common.response.*;
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
 * 预约用户
 *  +----------------------------------------------------------------------

 */
@Slf4j
@RestController
@RequestMapping("api/front/presaleuser")
@Api(tags = "预约用户")
public class PresaleUserController {

    @Autowired
    private StorePresaleUserService storePresaleUserService;

    /**
     * 预约用户列表
     */
    @ApiOperation(value = "预约用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StorePresaleUser>> list(@Validated StorePresaleUserSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storePresaleUserService.getH5List(request, pageParamRequest)));
    }

    /**
     * 去下单
     *
     * @param presaleId 预售商品id
     */
    @ApiOperation(value = "去预约")
    @RequestMapping(value = "/create_order/{presaleId}", method = RequestMethod.GET)
    public CommonResult<Object> createOrder(@PathVariable(value = "presaleId") Integer presaleId) {
        return CommonResult.success(storePresaleUserService.isCreateOrder(presaleId));
    }

}
