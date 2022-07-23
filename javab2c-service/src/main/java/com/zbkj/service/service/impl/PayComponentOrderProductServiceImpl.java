package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.wechat.video.PayComponentOrderProduct;
import com.zbkj.service.dao.PayComponentOrderProductDao;
import com.zbkj.service.service.PayComponentOrderProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class PayComponentOrderProductServiceImpl extends ServiceImpl<PayComponentOrderProductDao, PayComponentOrderProduct> implements PayComponentOrderProductService {

    @Resource
    private PayComponentOrderProductDao dao;

    /**
     * 获取订单商品列表
     * @param orderNo 订单编号
     * @return List
     */
    @Override
    public List<PayComponentOrderProduct> getListByOrderNo(String orderNo) {
        LambdaQueryWrapper<PayComponentOrderProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayComponentOrderProduct::getOrderNo, orderNo);
        return dao.selectList(lqw);
    }
}
