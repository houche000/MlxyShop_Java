package com.zbkj.common.response;

import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 拼团表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StorePinkDetailResponse对象", description="拼团详情响应对象")
public class StorePinkDetailResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "拼团ID")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "订单id 生成")
    private String orderId;

    @ApiModelProperty(value = "购买总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "开团金豆")
    private Integer ktIntegral;

    @ApiModelProperty(value = "鼓励佣金")
    private BigDecimal incentiveCommission;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "拼图总人数")
    private Integer people;

    @ApiModelProperty(value = "状态1进行中2已完成3未完成")
    private Integer status;

    @ApiModelProperty(value = "订单状态（0：待发货；1：待收货；2：已收货，待评价；3：已完成；）")
    private Integer orderStatus;

    @ApiModelProperty(value = "0 未退款 1 申请中 2 已退款 3退款中")
    private Integer refundStatus;

    @ApiModelProperty(value = "中奖人用户uid")
    private String winner;

    @ApiModelProperty(value = "中奖时间")
    private Long winnerTime;

    @ApiModelProperty(value = "是否系统拼团")
    private Boolean isSystem;

    @ApiModelProperty(value = "kid")
    private Integer kId;

    @ApiModelProperty(value = "订单信息")
    private StoreOrder storeOrder;

    @ApiModelProperty(value = "订单详情列表")
    private List<StoreOrderInfo> storeOrderInfos;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "结束时间")
    private Long stopTime;

}
