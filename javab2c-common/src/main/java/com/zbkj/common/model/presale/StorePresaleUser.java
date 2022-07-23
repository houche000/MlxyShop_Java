package com.zbkj.common.model.presale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预售用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_presale_user")
@ApiModel(value="StorePresaleUser对象", description="预售用户表")
public class StorePresaleUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "iD")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "是否系统预售用户")
    private Boolean isSystem;

    @ApiModelProperty(value = "预售用户商品id")
    private Integer presaleId;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "商品图片")
    private String image;

    @ApiModelProperty(value = "是否中奖 0待开奖 1未中奖 2已中奖")
    private Integer isWinner;

    @ApiModelProperty(value = "中奖时间")
    private Long winnerTime;

    @ApiModelProperty(value = "订单id 生成")
    private String orderId;

    @ApiModelProperty(value = "订单id  数据库")
    private Integer orderIdKey;

    @ApiModelProperty(value = "购买商品个数")
    private Integer totalNum;

    @ApiModelProperty(value = "购买总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "是否发送模板消息0未发送1已发送")
    private Boolean isTpl;

    @ApiModelProperty(value = "是否退款 0未退款 1已退款")
    private Boolean isRefund;

    @ApiModelProperty(value = "是否支付 0未退款 1已退款")
    private Boolean isPay;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;
}
