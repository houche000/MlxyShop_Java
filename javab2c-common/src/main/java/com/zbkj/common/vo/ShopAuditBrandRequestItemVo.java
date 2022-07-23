package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 上传品牌信息 request item
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopAuditBrandRequestItemVo {

    /** 营业执照或组织机构代码证，图片url/media_id */
    @NotBlank(message = "营业执照或组织机构代码证不能为空")
    private String license;

    /** 品牌信息 */
    @TableField(value = "brand_info")
    private ShopAuditBrandRequestItemDataVo brandInfo;
}
