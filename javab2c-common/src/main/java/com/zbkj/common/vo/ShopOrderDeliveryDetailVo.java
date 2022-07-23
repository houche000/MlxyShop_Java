package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 生成订单Vo对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopOrderDeliveryDetailVo {

    /** 1: 正常快递, 2: 无需快递, 3: 线下配送, 4: 用户自提 */
    @TableField(value = "delivery_type")
    private Integer delivery_type;

    /** 是否发货完成 */
    @TableField(value = "finish_all_delivery")
    private Integer finishAllDelivery;

    /** 交付数组 */
    @TableField(value = "delivery_list")
    private List<ShopOrderDeliveryInfoVo> deliveryList;
}
