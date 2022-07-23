package com.zbkj.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/26 10:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LuckLotteryExcelVo", description = "抽奖记录导出")
public class LuckLotteryExcelVo {

    @ApiModelProperty(value = "抽奖记录ID")
    private Integer id;

    @ApiModelProperty(value = "用户UID")
    private Integer uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "活动id")
    private Integer lotteryId;

    @ApiModelProperty(value = "活动名称")
    private String lotteryName;

    @ApiModelProperty(value = "奖品id")
    private Integer prizeId;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "是否中奖")
    private String isLuck;

    @ApiModelProperty(value = "抽奖时间")
    private String addTime;

    @ApiModelProperty(value = "是否领取")
    private String isReceive;

    @ApiModelProperty(value = "领取时间")
    private String receiveTime;

    @ApiModelProperty(value = "收获地址、备注等")
    private String receiveInfo;

    @ApiModelProperty(value = "是否发货")
    private String isDeliver;

    @ApiModelProperty(value = "发货处理时间")
    private String deliverTime;

    @ApiModelProperty(value = "发货单号、备注等")
    private String deliverInfo;


}
