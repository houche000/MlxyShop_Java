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
@TableName("eb_luck_lottery")
@ApiModel(value = "LuckLottery对象", description = "抽奖活动表")
public class LuckLottery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "抽奖活动表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "抽奖类型1:九宫格2：大转盘3：九宫格升级版 4：幸运翻牌")
    private Integer type;

    @ApiModelProperty(value = "抽奖活动名称")
    private String name;

    @ApiModelProperty(value = "活动描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty(value = "活动背景图")
    private String image;

    @ApiModelProperty(value = "抽奖消耗：1:金豆2：余额3：下单支付成功4：关注5：订单评价")
    private Integer factor;

    @ApiModelProperty(value = "获取一次抽奖的条件数量")
    private Integer factorNum;

    @ApiModelProperty(value = "参与用户1：所有2：部分")
    private Integer attendsUser;

    @ApiModelProperty(value = "参与用户等级")
    private Integer userLevel;

    @ApiModelProperty(value = "参与用户标签")
    private Integer userLabel;

    @ApiModelProperty(value = "参与用户是否付费会员")
    private Integer isSvip;

    @ApiModelProperty(value = "奖品数量")
    private Integer prizeNum;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

    @ApiModelProperty(value = "抽奖次数限制：1：每天2：每人")
    private Integer lotteryNumTerm;

    @ApiModelProperty(value = "抽奖次数")
    private Integer lotteryNum;

    @ApiModelProperty(value = "关注推广获取抽奖次数")
    private Integer spreadNum;

    @ApiModelProperty(value = "中奖纪录展示")
    private Integer isAllRecord;

    @ApiModelProperty(value = "个人中奖纪录展示")
    private Integer isPersonalRecord;

    @ApiModelProperty(value = "活动规格是否展示")
    private Integer isContent;

    @ApiModelProperty(value = "活动文案抽奖协议之类")
    private String content;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;

}
