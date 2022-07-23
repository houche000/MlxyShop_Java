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
 *  秒杀商品移动端对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreSecKillH5Response对象", description="秒杀商品移动端对象")
public class StoreSecKillH5Response implements Serializable {

    private static final long serialVersionUID = -885733985825623484L;

    @ApiModelProperty(value = "商品秒杀产品表id")
    private Integer id;

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

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "原价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "限购总数")
    private Integer quota;

    @ApiModelProperty(value = "已抢百分比")
    private Integer percent;
}
