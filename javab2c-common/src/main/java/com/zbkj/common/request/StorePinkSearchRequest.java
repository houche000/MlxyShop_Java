package com.zbkj.common.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 拼团表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_pink")
@ApiModel(value = "StorePink对象", description = "拼团表")
public class StorePinkSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("拼团id")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "拼团商品id")
    private Integer cid;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "关键词")
    private String keyWord;

    @ApiModelProperty(value = "状态1进行中2已完成3未完成")
    private Integer status;

    @ApiModelProperty(value = "是否系统团")
    private Boolean isSystem;

    @ApiModelProperty(value = "用户类型 0-机器人，1-用户")
    private Integer userType;

    @ApiModelProperty(value = "是否中奖")
    private Boolean isWinner;

    @ApiModelProperty(value = "参团时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String dateLimit;

    @ApiModelProperty(value = "结束时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String endDateLimit;

    @ApiModelProperty(value = "中奖时间 today,yesterday,lately7,lately30,month,year,/yyyy-MM-dd hh:mm:ss,yyyy-MM-dd hh:mm:ss/")
    private String winnerDateLimit;

}
