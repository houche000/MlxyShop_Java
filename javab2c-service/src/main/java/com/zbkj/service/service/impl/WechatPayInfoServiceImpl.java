package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.wechat.WechatPayInfo;
import com.zbkj.service.dao.WechatPayInfoDao;
import com.zbkj.service.service.WechatPayInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  WechatPayInfoServiceImpl 接口实现
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class WechatPayInfoServiceImpl extends ServiceImpl<WechatPayInfoDao, WechatPayInfo> implements WechatPayInfoService {

    @Resource
    private WechatPayInfoDao dao;

    /**
     * 获取详情（商户订单号）
     * @param outTradeNo 商户订单号
     * @return WechatPayInfo
     */
    @Override
    public WechatPayInfo getByNo(String outTradeNo) {
        LambdaQueryWrapper<WechatPayInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(WechatPayInfo::getOutTradeNo, outTradeNo);
        return dao.selectOne(lqw);
    }
}

