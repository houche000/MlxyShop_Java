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
public class ShopOrderAddVo {

    /** 创建时间 */
    @TableField(value = "create_time")
    private String createTime;

    /** 商家自定义订单ID */
    @TableField(value = "out_order_id")
    private String outOrderId;

    /** 用户的openid */
    private String openid;

    /** 商家小程序该订单的页面path，用于微信侧订单中心跳转 */
    private String path;

    /** 下单时小程序的场景值，可通getLaunchOptionsSync或onLaunch/onShow拿到 */
    private Integer scene;

    /** 订单详情 */
    @TableField(value = "order_detail")
    private ShopOrderDetailAddVo orderDetail;

    /** 交付详情 */
    @TableField(value = "delivery_detail")
    private ShopOrderDeliveryDetailAddVo deliveryDetail;

    /** 地址详情 */
    @TableField(value = "address_info")
    private ShopOrderAddressInfoAddVo addressInfo;

    /** 用户id */
    private Integer outUserId;
}
