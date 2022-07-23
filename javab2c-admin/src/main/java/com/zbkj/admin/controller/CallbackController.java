package com.zbkj.admin.controller;

import com.alibaba.fastjson.JSON;
import com.huifu.adapay.core.AdapayCore;
import com.huifu.adapay.core.util.AdapaySign;
import com.zbkj.common.response.WithdrawCallbackResponse;
import com.zbkj.service.service.CallbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 支付回调
 */
@Slf4j
@RestController
@RequestMapping("api/admin/payment/callback")
@Api(tags = "支付回调")
public class CallbackController {

    @Autowired
    private CallbackService callbackService;

    /**
     * 微信支付回调
     */
    @ApiOperation(value = "微信支付回调")
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public String weChat(@RequestBody String  request) {

        String response = callbackService.weChat(request);

        return response;
    }

    /**
     * 支付宝支付回调
     */
    @ApiOperation(value = "支付宝支付回调 ")
    @RequestMapping(value = "/alipay", method = RequestMethod.POST)
    public String aliPay(HttpServletRequest request){
        //支付宝支付回调

        return callbackService.aliPay(request);
    }

    /**
     * 微信退款回调
     */
    @ApiOperation(value = "微信退款回调")
    @RequestMapping(value = "/wechat/refund", method = RequestMethod.POST)
    public String weChatRefund(@RequestBody String request) {

        String response = callbackService.weChatRefund(request);

        return response;
    }


    /**
     * kab支付回调
     */
    @ApiOperation(value = "kab支付回调")
    @RequestMapping(value = "/kabpay")
    public String kabPay(HttpServletRequest request) {

        String response = callbackService.kabPay(request);
        System.out.println("kab支付回调结束：" +response);
        return response;
    }

    /**
     * 支付宝支付回调
     */
    @ApiOperation(value = "盈富支付回调 ")
    @RequestMapping(value = "/yyPay", method = RequestMethod.POST)
    public String yyPay(HttpServletRequest request) {
        //支付宝支付回调

        callbackService.yyPay(request);
        return "success";
    }

    /**
     * 盈富退款回调
     */
    @ApiOperation(value = "盈富退款回调")
    @RequestMapping(value = "/yypay/refund", method = RequestMethod.POST)
    public String yyPayRefund(HttpServletRequest request) {

        String response = callbackService.yyPayRefund(request);

        return "success";
    }

    /**
     * 盈富退款回调
     */
    @ApiOperation(value = "durianPay回调")
    @PostMapping("/durianPay")
    public String durianPay(@RequestBody Map<String, String> map) {


        String response = callbackService.durianPay(map);

        return response;
    }

    /**
     * 雷蛇支付回调
     */
    @ApiOperation(value = "razerPay回调")
    @PostMapping("/razerPay")
    public String razerPay(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();

        String response = callbackService.razerPay(request);

        return response;
    }

    /**
     * ada回调
     */
    @ApiOperation(value = "adaPay回调")
    @PostMapping("/adaPay")
    public void callback(HttpServletRequest request) {
        try {
            //验签请参data
            String data = request.getParameter("data");
            String type = request.getParameter("type");
            //验签请参sign
            String sign = request.getParameter("sign");
            //验签标记
            boolean checkSign;
            //验签请参publicKey
            String publicKey = AdapayCore.PUBLIC_KEY;
            log.info("验签请参：data={}sign={}");
            //验签
            checkSign = AdapaySign.verifySign(data, sign, publicKey);
            if (checkSign) {
                //验签成功逻辑

                Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
                if ("payment.succeeded".equals(type)) {
                    callbackService.adaPay(dataMap);
                } else if ("refund.succeeded".equals(type)) {
                    callbackService.adaPayRefund(dataMap);
                }
                return;
            } else {
                //验签失败逻辑
            }
        } catch (Exception e) {
            log.info("异步回调开始，参数，request={}");
        }
    }

    /**
     * 太平洋支付回调
     */
    @ApiOperation(value = "太平洋支付回调")
    @PostMapping("/withdraw")
    public void withdrawCallback(@RequestBody WithdrawCallbackResponse data) {


        Map<String, Object> backData = data.getData();
        Boolean status = data.getStatus();
        String ref_no = (String) backData.get("ref_no");

        System.out.println("提现返回数据 " + data.toString());
//        Map<String, Object> backData = data.getData();
//        System.out.println("backData " + backData.toString());
//        Boolean status = (Boolean) backData.get("status");
//        System.out.println("status " + status);
//        Map<String, Object> dataValue = (Map<String, Object>) backData.get("data");
//        System.out.println("dataValue " + dataValue.toString());
//        String ref_no = (String) dataValue.get("ref_no");
        int id = Integer.parseInt(ref_no.substring(0, ref_no.indexOf("_")));
        if (status==null  || !status){

            //提现反馈失败,将提现业务状态变更为提现失败
            callbackService.withdrawCallbackError(id);
        }
    }
}



