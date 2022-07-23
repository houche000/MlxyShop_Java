package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 *  自定义交易组件商品添加响应Vo
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopSpuAddResponseVo {

    /** 交易组件平台内部商品ID */
    @TableField(value = "product_id")
    private Integer productId;

    /** 商家自定义商品ID */
    @TableField(value = "out_product_id")
    private String outProductId;

    /** 创建时间 */
    @TableField(value = "create_time")
    private String createTime;

    /** 更新时间 */
    @TableField(value = "update_time")
    private String updateTime;

    /** sku数组 */
    private List<ShopSpuAddSkuResponseVo> skus;
}
