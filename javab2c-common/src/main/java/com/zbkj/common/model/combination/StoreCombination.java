package com.zbkj.common.model.combination;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拼团商品表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_combination")
@ApiModel(value="StoreCombination对象", description="拼团商品表")
public class StoreCombination implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "拼团商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "推荐图")
    private String image;

    @ApiModelProperty(value = "轮播图")
    private String images;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动标题-英语")
    @JsonProperty("title_EN")
    private String titleEn;

    @ApiModelProperty(value = "活动标题-马来西亚语")
    @JsonProperty("title_MY")
    private String titleMy;

    @ApiModelProperty(value = "活动属性")
    private String attr;

    @ApiModelProperty(value = "参团人数")
    private Integer people;

    @ApiModelProperty(value = "中奖人数")
    private Integer winnerPeople;

    @ApiModelProperty(value = "简介")
    private String info;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "开团价格")
    private BigDecimal ktPrice;

    @ApiModelProperty(value = "参团价格")
    private BigDecimal ctPrice;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "添加时间")
    private Long addTime;

    @ApiModelProperty(value = "推荐")
    private Boolean isHost;

    @ApiModelProperty(value = "商品状态")
    private Boolean isShow;

    private Boolean isDel;

    private Boolean combination;

    @ApiModelProperty(value = "商户是否可用1可用0不可用")
    private Boolean merUse;

    @ApiModelProperty(value = "是否包邮1是0否")
    private Boolean isPostage;

    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "拼团开始时间")
    private Long startTime;

    @ApiModelProperty(value = "拼团结束时间")
    private Long stopTime;

    @ApiModelProperty(value = "拼团订单有效时间(小时)")
    private Integer effectiveTime;

    @ApiModelProperty(value = "拼图商品成本")
    private BigDecimal cost;

    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "运费模板ID")
    private Integer tempId;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "单次购买数量")
    private Integer num;

    @ApiModelProperty(value = "限购总数")
    private Integer quota;

    @ApiModelProperty(value = "限量总数显示")
    private Integer quotaShow;

    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "每个订单可购买数量")
    private Integer onceNum;

    @ApiModelProperty(value = "虚拟成团百分比")
    private Integer virtualRation;

    @ApiModelProperty(value = "限量百分比")
    @TableField(exist = false)
    private Integer quotaPercent;

    @ApiModelProperty(value = "分类id")
    private String pinkCateId;

    @ApiModelProperty(value = "自动开团cron表达式")
    private String autoSystemCron;

    @ApiModelProperty(value = "是否系统自动开团")
    private Boolean autoSystem;
}
