package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.alipay.AliPayCallback;
import com.zbkj.service.dao.AliPayCallbackDao;
import com.zbkj.service.service.AliPayCallbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AliPayCallbackServiceImpl 接口实现
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class AliPayCallbackServiceImpl extends ServiceImpl<AliPayCallbackDao, AliPayCallback> implements AliPayCallbackService {

    @Resource
    private AliPayCallbackDao dao;

}

