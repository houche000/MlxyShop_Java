package com.zbkj.service.service;

import com.zbkj.common.response.HomeOperatingDataResponse;
import com.zbkj.common.response.HomeRateResponse;

import java.util.Map;

/**
 * 首页统计
 */
public interface HomeService{

    /**
     * 用户曲线图
     */
    Map<Object, Object> chartUser();

    /**
     * 30天订单量趋势
     */
    Map<String, Object> chartOrder();

    /**
     * 用户购买统计
     */
    Map<String, Integer> chartUserBuy();

    /**
     * 周订单量趋势
     */
    Map<String, Object> chartOrderInWeek();

    /**
     * 月订单量趋势
     */
    Map<String, Object> chartOrderInMonth();

    /**
     * 年订单量趋势
     */
    Map<String, Object> chartOrderInYear();

    /**
     * 首页数据
     * @return HomeRateResponse
     */
    HomeRateResponse indexDate();

    /**
     * 经营数据
     * @return HomeOperatingDataResponse
     */
    HomeOperatingDataResponse operatingData();
}
