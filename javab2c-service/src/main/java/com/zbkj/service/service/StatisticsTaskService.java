package com.zbkj.service.service;

/**
 * StatisticsTaskService 接口
 */
public interface StatisticsTaskService {

    /**
     * 每天零点的自动统计
     */
    void autoStatistics();

    /**
     * 根据日期获取统计数据(商城维度)
     * @param dateLimit 日期参数
     * @return ShoppingProductDataResponse
     */
//    ShoppingProductDataResponse getDataByDate(String dateLimit);
}
