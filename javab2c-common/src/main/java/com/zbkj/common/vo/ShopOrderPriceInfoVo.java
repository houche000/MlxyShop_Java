package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 生成订单Vo对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopOrderPriceInfoVo {

    /** 该订单最终的金额（单位：分） */
    @TableField(value = "order_price")
    private Long orderPrice;

    /** 运费（单位：分） */
    private Long freight;

    /** 优惠金额（单位：分） */
    @TableField(value = "discounted_price")
    private Long discountedPrice;

    /** 附加金额（单位：分） */
    @TableField(value = "additional_price")
    private Long additionalPrice;

    /** 附加金额备注 */
    @TableField(value = "additional_remarks")
    private String additional_remarks;
}
