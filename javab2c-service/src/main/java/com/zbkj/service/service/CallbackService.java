package com.zbkj.service.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 订单支付回调 service
 */
public interface CallbackService {
    /**
     * 微信支付回调
     * @param xmlInfo 微信回调json
     * @return String
     */
    String weChat(String xmlInfo);

    /**
     * 支付宝支付回调
     */
    String aliPay(HttpServletRequest request);



    /**
     * 微信退款回调
     *
     * @param request 微信回调json
     * @return String
     */
    String weChatRefund(String request);

    /**
     * 盈富支付回调
     *
     * @param request
     * @return
     */
    String yyPay(HttpServletRequest request);

    /**
     * 盈富退款回调
     *
     * @param request
     * @return
     */
    String yyPayRefund(HttpServletRequest request);

    /**
     * kab支付回调
     *
     * @param request
     * @return
     */
    String kabPay(HttpServletRequest request);

    /**
     * durianPay退款回调
     *
     * @param request
     * @return
     */
    String durianPay(Map<String, String> request);

    /**
     * adaPay回调
     *
     * @param request
     * @return
     */
    String adaPay(Map<String, Object> request);

    /**
     * adaPay退款回调
     *
     * @param request
     * @return
     */
    String adaPayRefund(Map<String, Object> request);

    String razerPay(HttpServletRequest request);

    //处理提现失败
    Boolean withdrawCallbackError(Integer id);
}
