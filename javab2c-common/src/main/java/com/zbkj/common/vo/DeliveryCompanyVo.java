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
public class DeliveryCompanyVo {

    /** 快递公司id */
    @TableField(value = "delivery_id")
    private String deliveryId;

    /** 快递公司名称 */
    @TableField(value = "delivery_name")
    private String deliveryName;

}
