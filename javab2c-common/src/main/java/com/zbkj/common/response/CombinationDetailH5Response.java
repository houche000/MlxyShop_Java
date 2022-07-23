package com.zbkj.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  拼团商品移动端对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CombinationDetailH5Response对象", description="拼团商品移动端对象")
public class CombinationDetailH5Response implements Serializable {

    private static final long serialVersionUID = -885733985825623484L;

    @ApiModelProperty(value = "拼团商品ID")
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "推荐图")
    private String image;

    @ApiModelProperty(value = "轮播图")
    private String sliderImage;

    @ApiModelProperty(value = "活动标题")
    private String storeName;

    @ApiModelProperty(value = "活动标题-英语")
    @JsonProperty("storeName_EN")
    private String storeNameEn;

    @ApiModelProperty(value = "活动标题-马来西亚语")
    @JsonProperty("storeName_MY")
    private String storeNameMy;

    @ApiModelProperty(value = "参团人数")
    private Integer people;

    @ApiModelProperty(value = "拼团结束时间")
    private Long stopTime;

    @ApiModelProperty(value = "简介")
    private String storeInfo;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "开团价格")
    private BigDecimal ktPrice;

    @ApiModelProperty(value = "参团价格")
    private BigDecimal ctPrice;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "限购总数")
    private Integer quota;

    @ApiModelProperty(value = "限量总数显示")
    private Integer quotaShow;

    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "每个订单可购买数量")
    private Integer onceNum;

    @ApiModelProperty(value = "虚拟销量")
    private Integer ficti;

    @ApiModelProperty(value = "商品详情")
    private String content;
}
