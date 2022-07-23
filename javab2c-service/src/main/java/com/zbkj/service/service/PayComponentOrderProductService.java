package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentOrderProduct;

import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentOrderProductService extends IService<PayComponentOrderProduct> {

    /**
     * 获取订单商品列表
     * @param orderNo 订单编号
     * @return List
     */
    List<PayComponentOrderProduct> getListByOrderNo(String orderNo);
}