package com.zbkj.service.service;

/**
 * 易联云打印订单 service
 */
public interface YlyPrintService {
    /**
     * 易联云打印商品信息
     * @param ordId 订单id
     * @param isAuto 是否自动打印
     */
    void YlyPrint(String ordId,boolean isAuto);
}
