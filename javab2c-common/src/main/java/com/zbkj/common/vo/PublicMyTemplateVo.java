package com.zbkj.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信公众号私有模板消息Vo对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PublicMyTemplateVo对象", description="微信公众号私有模板消息Vo对象")
public class PublicMyTemplateVo {

    @ApiModelProperty(value = "模板ID")
    private String template_id;

    @ApiModelProperty(value = "模板ID")
    private String title;

    @ApiModelProperty(value = "一级行业")
    private String primary_industry;

    @ApiModelProperty(value = "二级行业")
    private String deputy_industry;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "模板示例")
    private String example;
}
