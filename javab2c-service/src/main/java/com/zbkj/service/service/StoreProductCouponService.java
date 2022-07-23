package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.product.StoreProductCoupon;

import java.util.List;

/**
 * StoreProductCouponService 接口
 */
public interface StoreProductCouponService extends IService<StoreProductCoupon> {
    /**
     * 根据产品id删除 优惠券关联信息
     * @param productId 产品id
     */
    boolean deleteByProductId(Integer productId);

    /**
     * 根据商品id获取已关联优惠券信息
     * @param productId 商品id
     * @return 已关联优惠券
     */
    List<StoreProductCoupon> getListByProductId(Integer productId);
}
