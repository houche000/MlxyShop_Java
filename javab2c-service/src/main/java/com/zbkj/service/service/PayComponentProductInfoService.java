package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentProductInfo;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentProductInfoService extends IService<PayComponentProductInfo> {

    /**
     * 获取商品详情
     * @param proId 商品id
     * @return PayComponentProductInfo
     */
    PayComponentProductInfo getByProId(Integer proId);

    /**
     * 删除通过商品id
     * @param proId 商品id
     * @return Boolean
     */
    Boolean deleteByProId(Integer proId);
}