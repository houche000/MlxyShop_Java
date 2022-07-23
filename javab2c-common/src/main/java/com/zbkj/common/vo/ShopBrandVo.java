package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopBrandVo {

    /** 品牌ID */
    @TableField(value = "brand_id")
    private Integer brandId;

    /** 品牌名称 */
    @TableField(value = "brand_wording")
    private String brandWording;

}
