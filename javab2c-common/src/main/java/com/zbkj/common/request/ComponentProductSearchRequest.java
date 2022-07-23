package com.zbkj.common.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 组件商品列表搜索Request对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ComponentProductSearchRequest对象", description="组件商品列表搜索Request对象")
public class ComponentProductSearchRequest implements Serializable {

    private static final long serialVersionUID = -2196197495866986580L;

    @ApiModelProperty(value = "商品ID")
    private Integer proId;

    @ApiModelProperty(value = "模糊搜索内容，目前只支持商品名称")
    private String search;
}
