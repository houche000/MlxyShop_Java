package com.zbkj.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品所参与的活动类型
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ProductActivityItemResponse {

    @ApiModelProperty(value = "参与活动id")
    private Integer id;

    @ApiModelProperty(value = "活动结束时间")
    private Integer time;

    @ApiModelProperty(value = "活动参与类型:1=秒杀，2=砍价，3=拼团")
    private String type;
}
