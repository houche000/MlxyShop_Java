package com.zbkj.admin.controller;

import com.zbkj.service.service.PayComponentProductSkuService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *  组件商品sku表 前端控制器
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("api/admin/pay-component-product-sku")
@Api(tags = "组件商品sku表") //配合swagger使用

public class PayComponentProductSkuController {

    @Autowired
    private PayComponentProductSkuService payComponentProductSkuService;
}



