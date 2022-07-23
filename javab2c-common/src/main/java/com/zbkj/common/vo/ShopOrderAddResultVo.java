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
public class ShopOrderAddResultVo {

    /** 交易组件平台订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 交易组件平台订单ID */
    @TableField(value = "out_order_id")
    private String outOrderId;

    /** 拉起收银台的ticket */
    private String ticket;

    /** ticket有效截止时间 */
    @TableField(value = "ticket_expire_time")
    private String ticketExpireTime;

    /** 订单最终价格（单位：分） */
    @TableField(value = "final_price")
    private Long finalPrice;
}
