package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.wechat.video.PayComponentProductSku;
import com.zbkj.service.dao.PayComponentProductSkuDao;
import com.zbkj.service.service.PayComponentProductSkuService;
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
public class PayComponentProductSkuServiceImpl extends ServiceImpl<PayComponentProductSkuDao, PayComponentProductSku> implements PayComponentProductSkuService {

    @Resource
    private PayComponentProductSkuDao dao;

    /**
     * 通过商品id删除数据
     * @param proId 商品id
     * @return Boolean
     */
    @Override
    public Boolean deleteByProId(Integer proId) {
        LambdaUpdateWrapper<PayComponentProductSku> luw = Wrappers.lambdaUpdate();
        luw.set(PayComponentProductSku::getIsDel, true);
        luw.eq(PayComponentProductSku::getProductId, proId);
        luw.eq(PayComponentProductSku::getIsDel, false);
        return update(luw);
    }

    /**
     * 通过商品id获取列表
     * @param proId 商品id
     * @return List
     */
    @Override
    public List<PayComponentProductSku> getListByProId(Integer proId) {
        LambdaQueryWrapper<PayComponentProductSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayComponentProductSku::getProductId, proId);
        lqw.eq(PayComponentProductSku::getIsDel, false);
        return dao.selectList(lqw);
    }

    /**
     * 通过商品id，规格属性id获取对象
     * @param proId 商品id
     * @param attrValueId 规格属性id
     * @return PayComponentProductSku
     */
    @Override
    public PayComponentProductSku getByProIdAndAttrValueId(Integer proId, Integer attrValueId) {
        LambdaQueryWrapper<PayComponentProductSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayComponentProductSku::getProductId, proId);
        lqw.eq(PayComponentProductSku::getAttrValueId, attrValueId);
        lqw.eq(PayComponentProductSku::getIsDel, false);
        return dao.selectOne(lqw);
    }

    /**
     * 添加/扣减库存
     * @param skuId skuId
     * @param num 数量
     * @param operationType 类型：add—添加，sub—扣减
     * @return Boolean
     */
    @Override
    public Boolean operationStock(Integer skuId, Integer num, String operationType) {
        UpdateWrapper<PayComponentProductSku> updateWrapper = new UpdateWrapper<>();
        if (operationType.equals("add")) {
            updateWrapper.setSql(StrUtil.format("stock_num = stock_num + {}", num));
//            updateWrapper.setSql(StrUtil.format("sales = sales - {}", num));
        }
        if (operationType.equals("sub")) {
            updateWrapper.setSql(StrUtil.format("stock_num = stock_num - {}", num));
//            updateWrapper.setSql(StrUtil.format("sales = sales + {}", num));
            // 扣减时加乐观锁保证库存不为负
            updateWrapper.last(StrUtil.format("and (stock_num - {} >= 0)", num));
        }
        updateWrapper.eq("id", skuId);
        boolean update = update(updateWrapper);
        if (!update) {
            throw new CrmebException("更新组件商品SKU失败，SKUId = " + skuId);
        }
        return update;
    }
}

