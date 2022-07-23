package com.zbkj.common.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 预售用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_presale_user")
@ApiModel(value = "StorePresaleUser对象", description = "预售用户表")
public class StorePresaleUserSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "预售商品id")
    private Integer presaleId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "关键词")
    private String keyWord;

    @ApiModelProperty(value = "是否当前用户")
    private Integer isThisUser;

    @ApiModelProperty(value = "是否系统团")
    private Boolean isSystem;

    @ApiModelProperty(value = "用户类型 0-机器人，1-用户")
    private Integer userType;

    @ApiModelProperty(value = "是否中奖 0待开奖 1未中奖 2已中奖")
    private Integer isWinner;

    @ApiModelProperty(value = "参团时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String dateLimit;

    @ApiModelProperty(value = "结束时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String endDateLimit;

    @ApiModelProperty(value = "中奖时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String winnerDateLimit;

    @ApiModelProperty(value = "类型 1：预约记录 2：预约结果")
    private Integer type;
}
