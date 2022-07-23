package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.product.StoreProductDescription;

/**
 * StoreProductDescriptionService 接口
 */
public interface StoreProductDescriptionService extends IService<StoreProductDescription> {

    /**
     * 根据商品id和type删除对应描述
     * @param productId 商品id
     * @param type      类型
     */
    void deleteByProductId(int productId,int type);

    /**
     * 获取详情
     * @param productId 商品id
     * @param type 商品类型
     * @return
     */
    StoreProductDescription getByProductIdAndType(Integer productId, Integer type);
}
