package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.alipay.AliPayInfo;
import com.zbkj.service.dao.AliPayInfoDao;
import com.zbkj.service.service.AliPayInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AliPayInfoServiceImpl 接口实现
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class AliPayInfoServiceImpl extends ServiceImpl<AliPayInfoDao, AliPayInfo> implements AliPayInfoService {

    @Resource
    private AliPayInfoDao dao;



}

