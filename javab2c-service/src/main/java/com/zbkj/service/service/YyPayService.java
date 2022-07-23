package com.zbkj.service.service;

import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.request.StoreOrderRefundRequest;

/**
 * 支付宝支付 Service
 */
public interface YyPayService {


    /**
     * 查询支付结果
     *
     * @param orderNo 订单编号
     * @return
     */
    Boolean queryPayResult(String orderNo);

    void refund(StoreOrderRefundRequest request, StoreOrder storeOrder);

    /**
     * 查询退款
     *
     * @param orderNo 订单编号
     */
    Boolean queryRefund(String orderNo);
}
