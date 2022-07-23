package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class DeliveryInfoVo {

    /** 快递公司ID，通过获取快递公司列表获取 */
    @TableField(value = "delivery_id")
    private String deliveryId;

    /** 快递单号 */
    @TableField(value = "waybill_id")
    private String waybillId;
}
