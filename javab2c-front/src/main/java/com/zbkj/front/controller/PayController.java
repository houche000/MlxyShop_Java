package com.zbkj.front.controller;

import com.zbkj.common.request.OrderPayRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.OrderPayResultResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.service.service.*;
import com.zbkj.service.service.impl.DurianPayServiceImpl;
import com.zbkj.service.util.yly.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信缓存表 前端控制器
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 *  +----------------------------------------------------------------------
 */
@Slf4j
@RestController
@RequestMapping("api/front/pay")
@Api(tags = "支付管理")
public class PayController {

    @Autowired
    private WeChatPayService weChatPayService;

    @Autowired
    private RazerPayService razerPayService;

    @Autowired
    private DurianPayServiceImpl durianPayService;

    @Autowired
    private AdaPayService adaPayService;

    @Autowired
    private YyPayService yyPayService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private KabPayService kabPayService;

    /**
     * 订单支付
     */
    @ApiOperation(value = "订单支付")
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public CommonResult<OrderPayResultResponse> payment(@RequestBody @Validated OrderPayRequest orderPayRequest, HttpServletRequest request) {
        String ip = CrmebUtil.getClientIp(request);
        return CommonResult.success(orderPayService.payment(orderPayRequest, ip));
    }

    /**
     * 查询支付结果
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询支付结果")
    @RequestMapping(value = "/queryPayResult", method = RequestMethod.GET)
    public CommonResult<Boolean> queryPayResult(@RequestParam String orderNo) {
        return CommonResult.success(weChatPayService.queryPayResult(orderNo));
    }

    /**
     * 查询支付结果(富盈支付)
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询支付结果")
    @RequestMapping(value = "/queryYyPayResult", method = RequestMethod.GET)
    public CommonResult<Boolean> queryYyPayResult(@RequestParam String orderNo) {
        return CommonResult.success(yyPayService.queryPayResult(orderNo));
    }

    /**
     * 查询支付结果（kab支付）
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询支付结果")
    @RequestMapping(value = "/kabPayResult", method = RequestMethod.GET)
    public CommonResult<Object> kabPayResult(@RequestParam String orderNo) {
        return CommonResult.success(kabPayService.queryPayResult(orderNo));
    }

    /**
     * 查询雷蛇支付结果(富盈支付)
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询雷蛇支付结果")
    @RequestMapping(value = "/queryRazerPayResult", method = RequestMethod.GET)
    public CommonResult<Object> queryRazerPayResult(@RequestParam String orderNo) {
        return CommonResult.success(razerPayService.queryPayResult(orderNo));
    }

    /**
     * 查询durian支付结果
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询durian支付结果")
    @RequestMapping(value = "/queryDurianPayResult", method = RequestMethod.GET)
    public CommonResult<Object> queryDurianPayResult(@RequestParam String orderNo) {
        return CommonResult.success(durianPayService.queryPayResult(orderNo));
    }

    /**
     * 查询kab支付结果
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询kab支付结果")
    @RequestMapping(value = "/queryKabPayResult", method = RequestMethod.GET)
    public CommonResult<Object> queryKabPayResult(@RequestParam String orderNo) {
        return CommonResult.success(durianPayService.queryPayResult(orderNo));
    }

    /**
     * 查询ada支付结果
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询ada支付结果")
    @RequestMapping(value = "/queryAdaPayResult", method = RequestMethod.GET)
    public CommonResult<Object> queryAdaPayResult(@RequestParam String orderNo) {
        return CommonResult.success(adaPayService.queryPayResult(orderNo));
    }

    /**
     * 查询支付结果(支付宝)
     *
     * @param orderNo |订单编号|String|必填
     */
    @ApiOperation(value = "查询支付结果(支付宝)")
    @RequestMapping(value = "/queryAliPayResult", method = RequestMethod.GET)
    public CommonResult<Boolean> queryAliPayResult(@RequestParam String orderNo) {
        return CommonResult.success(aliPayService.queryPayResult(orderNo));
    }
}
