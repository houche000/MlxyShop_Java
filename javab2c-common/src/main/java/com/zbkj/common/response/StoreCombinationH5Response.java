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
@ApiModel(value="StoreCombinationH5Response对象", description="拼团商品移动端对象")
public class StoreCombinationH5Response implements Serializable {

    private static final long serialVersionUID = -885733985825623484L;

    @ApiModelProperty(value = "拼团商品ID")
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "推荐图")
    private String image;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动标题-英语")
    @JsonProperty("title_EN")
    private String titleEn;

    @ApiModelProperty(value = "活动标题-马来西亚语")
    @JsonProperty("title_MY")
    private String titleMy;

    @ApiModelProperty(value = "拼团分类id", required = true)
    private String pinkCateId;

    @ApiModelProperty(value = "参团人数")
    private Integer people;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "开团价格")
    private BigDecimal ktPrice;

    @ApiModelProperty(value = "参团价格")
    private BigDecimal ctPrice;

    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "商品详情")
    private String content;

    @ApiModelProperty(value = "收藏标识")
    private boolean userCollect;
}
