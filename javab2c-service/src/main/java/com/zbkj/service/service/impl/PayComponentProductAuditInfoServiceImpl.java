package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.wechat.video.PayComponentProductAuditInfo;
import com.zbkj.service.dao.PayComponentProductAuditInfoDao;
import com.zbkj.service.service.PayComponentProductAuditInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class PayComponentProductAuditInfoServiceImpl extends ServiceImpl<PayComponentProductAuditInfoDao, PayComponentProductAuditInfo> implements PayComponentProductAuditInfoService {

    @Resource
    private PayComponentProductAuditInfoDao dao;

    /**
     * 获取最后一条商品审核信息
     * @param productId 商品id
     * @param auditId 审核单id
     * @return PayComponentProductAuditInfo
     */
    @Override
    public PayComponentProductAuditInfo getByProductIdAndAuditId(Integer productId, String auditId) {
        LambdaQueryWrapper<PayComponentProductAuditInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayComponentProductAuditInfo::getProductId, productId);
        lqw.eq(PayComponentProductAuditInfo::getAuditId, auditId);
        return dao.selectOne(lqw);
    }
}

