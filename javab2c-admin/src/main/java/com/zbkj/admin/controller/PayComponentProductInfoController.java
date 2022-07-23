package com.zbkj.admin.controller;

import com.zbkj.service.service.PayComponentProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *  组件商品详情表 前端控制器
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("api/admin/pay-component-product-info")
@Api(tags = "组件商品详情表") //配合swagger使用
public class PayComponentProductInfoController {

    @Autowired
    private PayComponentProductInfoService payComponentProductInfoService;
}



