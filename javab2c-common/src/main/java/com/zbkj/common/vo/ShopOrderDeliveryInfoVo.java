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
public class ShopOrderDeliveryInfoVo {

    /** 快递公司ID，通过获取快递公司列表获取 */
    @TableField(value = "delivery_id")
    private Integer deliveryId;

    /** 快递单号 */
    @TableField(value = "waybill_id")
    private Integer waybillId;
}
