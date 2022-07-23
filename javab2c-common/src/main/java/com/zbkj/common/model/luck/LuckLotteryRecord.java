package com.zbkj.common.model.luck;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_luck_lottery_record")
@ApiModel(value = "LuckLotteryRecord对象", description = "抽奖记录表")
public class LuckLotteryRecord {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "抽奖记录表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户uid")
    private Integer uid;

    @ApiModelProperty(value = "活动id")
    private Integer lotteryId;

    @ApiModelProperty(value = "奖品id")
    private Integer prizeId;

    @ApiModelProperty(value = "奖品类型1：未中奖2：金豆3:余额4：红包5:优惠券6：站内商品7：等级经验8：用户等级 9：svip天数")
    private Integer type;

    @ApiModelProperty(value = "是否领取")
    private Boolean isReceive;

    @ApiModelProperty(value = "领取时间")
    private Long receiveTime;

    @ApiModelProperty(value = "收获地址、备注等")
    private String receiveInfo;

    @ApiModelProperty(value = "是否发货")
    private Integer isDeliver;

    @ApiModelProperty(value = "发货处理时间")
    private Long deliverTime;

    @ApiModelProperty(value = "发货单号、备注等")
    private String deliverInfo;

    @ApiModelProperty(value = "创建时间")
    private Long addTime;
}
