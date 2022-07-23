package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.wechat.video.PayComponentProductSkuAttr;
import com.zbkj.service.dao.PayComponentProductSkuAttrDao;
import com.zbkj.service.service.PayComponentProductSkuAttrService;
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
public class PayComponentProductSkuAttrServiceImpl extends ServiceImpl<PayComponentProductSkuAttrDao, PayComponentProductSkuAttr> implements PayComponentProductSkuAttrService {

    @Resource
    private PayComponentProductSkuAttrDao dao;

}

