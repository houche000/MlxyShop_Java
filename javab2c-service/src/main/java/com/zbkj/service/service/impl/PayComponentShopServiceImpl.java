package com.zbkj.service.service.impl;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.wechat.video.PayComponentShopBrand;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.ShopAuditBrandRequestVo;
import com.zbkj.service.service.PayComponentShopBrandService;
import com.zbkj.service.service.PayComponentShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class PayComponentShopServiceImpl implements PayComponentShopService {

    @Autowired
    private PayComponentShopBrandService payComponentShopBrandService;

    /**
     * 上传品牌
     * @param request 上传品牌请求参数
     * @return 审核单id
     */
    @Override
    public String auditBrand(ShopAuditBrandRequestVo request) {
        return payComponentShopBrandService.auditBrand(request);
    }

    /**
     * 获取品牌列表
     * @param pageParamRequest 分页参数
     * @param status 审核状态, 0：审核中，1：审核成功，9：审核拒绝
     * @return 品牌列表
     */
    @Override
    public PageInfo<PayComponentShopBrand> brandList(PageParamRequest pageParamRequest, Integer status) {
        return payComponentShopBrandService.findList(pageParamRequest, status);
    }

    /**
     * 获取品牌列表（可用）
     * @return 品牌列表
     */
    @Override
    public List<PayComponentShopBrand> usableBrandList() {
        return payComponentShopBrandService.getUsableList();
    }
}
