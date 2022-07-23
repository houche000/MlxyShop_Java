package com.zbkj.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户参与砍价响应体
 */
@Data
public class StoreBargainUserResponse {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户参与砍价表ID")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "砍价商品id")
    private Integer bargainId;

    @ApiModelProperty(value = "砍价的最低价")
    private BigDecimal bargainPriceMin;

    @ApiModelProperty(value = "砍价金额")
    private BigDecimal bargainPrice;

    @ApiModelProperty(value = "砍掉的价格")
    private BigDecimal price;

    @ApiModelProperty(value = "状态 1参与中 2 活动结束参与失败 3活动结束参与成功")
    private Integer status;

    @ApiModelProperty(value = "参与时间")
    private String addTime;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "结束时间")
    private String dataTime;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "当前价")
    private BigDecimal nowPrice;

    @ApiModelProperty(value = "剩余砍价次数")
    private Integer num;

    @ApiModelProperty(value = "总砍价次数")
    private Integer peopleNum;

    @ApiModelProperty(value = "砍价商品")
    private String title;
}
