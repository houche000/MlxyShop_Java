package com.zbkj.service.service;


import com.zbkj.common.vo.ShopAftersaleAddVo;
import com.zbkj.common.vo.ShopAftersaleUpdateVo;
import com.zbkj.common.vo.ShopAftersaleVo;
import com.zbkj.common.vo.ShopOrderCommonVo;

/**
 * 视频号交易组件服务——售后部分
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface WechatVideoAftersaleService {

    /**
     * 创建售后
     * @return Boolean
     */
    Boolean shopAftersaleAdd(ShopAftersaleAddVo shopAftersaleAddVo);

    /**
     * 获取售后
     * @return ShopAftersaleVo
     */
    ShopAftersaleVo shopAftersaleGet(ShopOrderCommonVo shopOrderCommonVo);

    /**
     * 更新售后
     * @return Boolean
     */
    Boolean shopAftersaleUpdate(ShopAftersaleUpdateVo shopAftersaleUpdateVo);
}
