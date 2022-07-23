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
public class AftersaleProductInfoVo {

    /** 商家自定义商品ID */
    @TableField(value = "out_product_id")
    private String outProductId;

    /** 商家自定义sku ID, 如果没有则不填 */
    @TableField(value = "out_sku_id")
    private String outSkuId;

    /** 参与售后的商品数量 */
    @TableField(value = "product_cnt")
    private Integer productCnt;

}
