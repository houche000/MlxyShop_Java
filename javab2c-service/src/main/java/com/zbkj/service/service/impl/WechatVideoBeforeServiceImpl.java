package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.constants.WeChatConstants;
import com.zbkj.common.utils.RestTemplateUtil;
import com.zbkj.common.utils.WxUtil;
import com.zbkj.common.vo.*;
import com.zbkj.service.service.WechatNewService;
import com.zbkj.service.service.WechatVideoBeforeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class WechatVideoBeforeServiceImpl implements WechatVideoBeforeService {

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private WechatNewService wechatNewService;

    /**
     * 上传图片
     *
     * @return 图片上传结果
     */
    @Override
    public WechatVideoUploadImageResponseVo shopImgUpload(MultipartFile multipart, String media) {
        String miniAccessToken = wechatNewService.getMiniAccessToken();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("media", media);
        String uploadResult = restTemplateUtil.postFormData(StrUtil.format(WeChatConstants.WECHAT_SHOP_SPU_ADD_URL, miniAccessToken),
                params);
        return JSON.toJavaObject(JSON.parseObject(uploadResult), WechatVideoUploadImageResponseVo.class);
    }

    /**
     * 上传品牌信息
     *
     * @param requestVo 待上传品牌参数
     * @return 审核单Id
     */
    @Override
    public ShopAuditBrandResponseVo shopAuditBrand(ShopAuditBrandRequestVo requestVo) {
        // 参数校验
        Map<String, Object> brandMap = assembleAuditBrandMap(requestVo);
        // 获取accessToken
        String miniAccessToken = wechatNewService.getMiniAccessToken();
        // 请求微信接口
        String url = StrUtil.format(WeChatConstants.WECHAT_SHOP_AUDIT_AUDIT_BRAND, miniAccessToken);
        String postStringData = restTemplateUtil.postStringData(url, JSONObject.toJSONString(brandMap));

        JSONObject jsonObject = JSONObject.parseObject(postStringData);
        WxUtil.checkResult(jsonObject);
        ShopAuditBrandResponseVo brandResponseVo = JSONObject.parseObject(postStringData, ShopAuditBrandResponseVo.class);
        return brandResponseVo;
    }

    /**
     * 组装上传品牌Map对象
     * @param requestVo 上传品牌请求参数
     * @return 商品品牌Map
     */
    private Map<String, Object> assembleAuditBrandMap(ShopAuditBrandRequestVo requestVo) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> infoMap = new HashMap<>();
        ShopAuditBrandRequestItemVo requestItemVo = requestVo.getAuditReq();
        ShopAuditBrandRequestItemDataVo brandInfo = requestItemVo.getBrandInfo();
        infoMap.put("brand_audit_type", brandInfo.getBrandAuditType());
        infoMap.put("trademark_type", brandInfo.getTrademarkType());
        infoMap.put("brand_management_type", brandInfo.getBrandManagementType());
        infoMap.put("commodity_origin_type", brandInfo.getCommodityOriginType());
        infoMap.put("brand_wording", brandInfo.getBrandWording());

        if (CollUtil.isNotEmpty(brandInfo.getTrademarkChangeCertificate())) {
            infoMap.put("trademark_change_certificate", brandInfo.getTrademarkChangeCertificate());
        }
        infoMap.put("trademark_registrant_nu", brandInfo.getTrademarkRegistrantNu());
        if (CollUtil.isNotEmpty(brandInfo.getImportedGoodsForm())) {
            infoMap.put("imported_goods_form", brandInfo.getImportedGoodsForm());
        }
        switch (brandInfo.getBrandAuditType()) {
            case 1:
                infoMap.put("trademark_registration_certificate", brandInfo.getTrademarkRegistrationCertificate());
                infoMap.put("trademark_registrant", brandInfo.getTrademarkRegistrant());
                infoMap.put("trademark_authorization_period", brandInfo.getTrademarkAuthorizationPeriod());
                break;
            case 2:
                infoMap.put("trademark_registration_application", brandInfo.getTrademarkRegistrationApplication());
                infoMap.put("trademark_applicant", brandInfo.getTrademarkApplicant());
                infoMap.put("trademark_application_time", brandInfo.getTrademarkApplicationTime());
                break;
            case 3:
                infoMap.put("sale_authorization", brandInfo.getSaleAuthorization());
                infoMap.put("trademark_registration_certificate", brandInfo.getTrademarkRegistrationCertificate());
                infoMap.put("trademark_registrant", brandInfo.getTrademarkRegistrant());
                infoMap.put("trademark_authorization_period", brandInfo.getTrademarkAuthorizationPeriod());
                break;
            case 4:
                infoMap.put("sale_authorization", brandInfo.getSaleAuthorization());
                infoMap.put("trademark_registration_application", brandInfo.getTrademarkRegistrationApplication());
                infoMap.put("trademark_applicant", brandInfo.getTrademarkApplicant());
                infoMap.put("trademark_application_time", brandInfo.getTrademarkApplicationTime());
                break;
            default:
                infoMap.put("sale_authorization", brandInfo.getSaleAuthorization());
                infoMap.put("trademark_registration_certificate", brandInfo.getTrademarkRegistrationCertificate());
//                infoMap.put("trademark_change_certificate", brandInfo.getTrademarkChangeCertificate());
                infoMap.put("trademark_registrant", brandInfo.getTrademarkRegistrant());
//                infoMap.put("trademark_registrant_nu", brandInfo.getTrademarkRegistrantNu());
                infoMap.put("trademark_authorization_period", brandInfo.getTrademarkAuthorizationPeriod());
                infoMap.put("trademark_registration_application", brandInfo.getTrademarkRegistrationApplication());
                infoMap.put("trademark_applicant", brandInfo.getTrademarkApplicant());
                infoMap.put("trademark_application_time", brandInfo.getTrademarkApplicationTime());
//                infoMap.put("imported_goods_form", brandInfo.getImportedGoodsForm());
                break;
        }
        reqMap.put("license", requestItemVo.getLicense());
        reqMap.put("brand_info", infoMap);

        map.put("audit_req", reqMap);
        return map;
    }

    /**
     * 上传类目资质
     *
     * @param requestVo 类目资质参数
     * @return 类目资质结果
     */
    @Override
    public ShopAuditCategoryResponseVo shopAuditCategory(ShopAuditCategoryRequestVo requestVo) {
        return null;
    }

    /**
     * 获取类目审核结果
     *
     * @param request 待审核类目id
     * @return 审核结果
     */
    @Override
    public ShopAuditResultResponseVo shopAuditResult(HashMap<String, String> request) {
        return null;
    }

    @Override
    public void shopAuditGetMinCertificate(HashMap<String, String> request) {

    }
}
