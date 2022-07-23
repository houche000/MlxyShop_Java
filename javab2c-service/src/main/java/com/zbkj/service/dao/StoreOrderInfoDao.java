package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.order.StoreOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 订单购物详情表 Mapper 接口
 */
public interface StoreOrderInfoDao extends BaseMapper<StoreOrderInfo> {

    /**
     * 根据时间、商品id获取销售件数
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     */
    Integer getSalesNumByDateAndProductId(@Param("date") String date, @Param("proId")  Integer proId);

    /**
     * 根据时间、商品id获取销售额
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     */
    BigDecimal getSalesByDateAndProductId(@Param("date") String date, @Param("proId")  Integer proId);
}
