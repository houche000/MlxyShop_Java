package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentProductAuditInfo;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentProductAuditInfoService extends IService<PayComponentProductAuditInfo> {

    /**
     * 获取最后一条商品审核信息
     * @param productId 商品id
     * @param auditId 审核单id
     * @return PayComponentProductAuditInfo
     */
    PayComponentProductAuditInfo getByProductIdAndAuditId(Integer productId, String auditId);
}