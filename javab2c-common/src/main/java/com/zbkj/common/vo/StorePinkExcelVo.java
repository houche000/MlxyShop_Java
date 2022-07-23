package com.zbkj.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StorePinkExcelVo {
    @ApiModelProperty(value = "拼团ID")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "订单id 生成")
    private String orderId;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty("商品信息")
    private String productInfo;

    @ApiModelProperty(value = "购买总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "支付方式")
    private String payType;

    @ApiModelProperty(value = "拼团角色")
    private String userRole;

    @ApiModelProperty(value = "用户身份")
    private String userType;

    @ApiModelProperty(value = "是否中奖")
    private String isWinner;

    @ApiModelProperty(value = "开团金豆")
    private Integer ktIntegral;

    @ApiModelProperty(value = "鼓励佣金")
    private BigDecimal incentiveCommission;

    @ApiModelProperty(value = "状态1进行中2已完成3未完成")
    private String status;

    @ApiModelProperty(value = "订单状态（0：待发货；1：待收货；2：已收货，待评价；3：已完成；）")
    private String orderStatus;

    @ApiModelProperty(value = "0 未退款 1 申请中 2 已退款 3退款中")
    private String refundStatus;

    @ApiModelProperty(value = "收货人")
    private String realName;

    @ApiModelProperty(value = "收货电话")
    private String phoneNumber;

    @ApiModelProperty(value = "收货地址")
    private String address;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "结束时间")
    private String stopTime;
}
