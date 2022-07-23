package com.zbkj.common.vo;

import lombok.Data;

/**
 * 上传类目资质Item
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopAuditCategoryRequestItemVo {
    // 营业执照或组织机构代码证，图片url
    private String license;
    // Response ItemData
    private ShopAuditCategoryRequestItemDataVo category_info;
}
