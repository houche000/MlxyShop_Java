package com.zbkj.service.service;

import com.zbkj.common.model.finance.UserRecharge;

import java.util.Map;

/**
 * 微信支付
 */
public interface WeChatPayService {

    /**
     * 查询支付结果
     * @param orderNo 订单编号
     * @return
     */
    Boolean queryPayResult(String orderNo);

    /**
     * 微信充值预下单接口
     * @param userRecharge 充值订单
     * @param clientIp      ip
     * @return 获取wechat.requestPayment()参数
     */
    Map<String, String> unifiedRecharge(UserRecharge userRecharge, String clientIp);
}
