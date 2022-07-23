package com.zbkj.service.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.wechat.video.PayComponentShopBrand;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.ShopAuditBrandRequestVo;

import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentShopService {

    /**
     * 上传品牌
     * @param request 上传品牌请求参数
     * @return 审核单id
     */
    String auditBrand(ShopAuditBrandRequestVo request);

    /**
     * 获取品牌列表
     * @param pageParamRequest 分页参数
     * @param status 审核状态, 0：审核中，1：审核成功，9：审核拒绝
     * @return 品牌列表
     */
    PageInfo<PayComponentShopBrand> brandList(PageParamRequest pageParamRequest, Integer status);

    /**
     * 获取品牌列表（可用）
     * @return 品牌列表
     */
    List<PayComponentShopBrand> usableBrandList();
}