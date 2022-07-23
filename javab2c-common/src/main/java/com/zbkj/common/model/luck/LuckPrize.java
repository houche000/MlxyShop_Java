package com.zbkj.common.model.luck;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("eb_luck_prize")
@ApiModel(value = "LuckPrize对象", description = "奖品表")
public class LuckPrize {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "奖品表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "奖品类型1：未中奖2：金豆3:余额4：红包5:优惠券6：站内商品7：等级经验8：用户等级 9：svip天数")
    private Integer type;

    @ApiModelProperty(value = "活动id")
    private Integer lotteryId;

    @ApiModelProperty(value = "奖品名称")
    private String name;

    @ApiModelProperty(value = "中奖提示语")
    private String prompt;

    @ApiModelProperty(value = "奖品图片")
    private String image;

    @ApiModelProperty(value = "中奖基数")
    private Integer chance;

    @ApiModelProperty(value = "奖品数量")
    private Integer total;

    @ApiModelProperty(value = "关联优惠券id")
    private Integer couponId;

    @ApiModelProperty(value = "关联商品id")
    private Integer productId;

    @ApiModelProperty(value = "关联商品规格唯一值")
    @TableField("`unique`")
    private String unique;

    @ApiModelProperty(value = "金豆 经验 会员天数")
    private Integer num;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;
}
