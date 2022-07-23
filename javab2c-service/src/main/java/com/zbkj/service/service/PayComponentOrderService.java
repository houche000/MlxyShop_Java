package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentOrder;
import com.zbkj.common.vo.ShopOrderAddVo;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentOrderService extends IService<PayComponentOrder> {

    /**
     * 创建组件订单
     * @param shopOrderAddVo 创建订单参数
     * @return ticket
     */
    String create(ShopOrderAddVo shopOrderAddVo);

    /**
     * 通过订单号获取订单
     * @param orderNo 订单编号
     * @return PayComponentOrder
     */
    PayComponentOrder getByOrderNo(String orderNo);

    /**
     * 创建售后
     * @param orderNo 订单编号
     */
    void createAfterSale(String orderNo);
}