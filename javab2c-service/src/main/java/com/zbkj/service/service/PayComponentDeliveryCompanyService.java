package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentDeliveryCompany;

import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentDeliveryCompanyService extends IService<PayComponentDeliveryCompany> {

    /**
     * 更新物流公司数据
     */
    void updateData();

    /**
     * 获取组件物流公司列表
     * @return List
     */
    List<PayComponentDeliveryCompany> getList();

    /**
     * 通过快递公司ID获取
     * @param deliveryId 快递公司ID
     * @return PayComponentDeliveryCompany
     */
    PayComponentDeliveryCompany getByDeliveryId(String deliveryId);
}