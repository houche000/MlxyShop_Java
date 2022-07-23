package com.zbkj.service.service;

import com.zbkj.common.model.record.ShoppingProductDayRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ProductRankingRequest;
import com.zbkj.common.response.ProductRankingResponse;
import com.zbkj.common.response.ShoppingProductDataResponse;

import java.util.List;

/**
 * ProductStatisticsService 接口
 */
public interface ProductStatisticsService {

    /**
     * 根据日期获取统计数据(商城维度)
     * @param dateLimit 日期参数
     * @return ShoppingProductDataResponse
     */
    ShoppingProductDataResponse getDataByDate(String dateLimit);

    /**
     * 获取商品排行榜
     * @param request 查询参数
     * @return CommonPage
     */
    CommonPage<ProductRankingResponse> getRanking(ProductRankingRequest request);

    /**
     * 商品趋势数据
     * @param dateLimit 日期参数
     * @return List
     */
    List<ShoppingProductDayRecord> getTrendDataByDate(String dateLimit);
}
