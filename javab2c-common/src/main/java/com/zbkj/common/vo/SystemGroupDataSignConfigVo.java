package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 签到记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SystemGroupDataSignConfigVo对象", description = "签到记录")
public class SystemGroupDataSignConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty(value = "显示文字")
    private String title;

    @ApiModelProperty(value = "显示文字-英语")
    @JsonProperty("title_EN")
    private String titleEn;

    @ApiModelProperty(value = "显示文字-马来西亚语")
    @JsonProperty("title_MY")
    private String titleMy;

    @ApiModelProperty(value = "第几天")
    private Integer day;


    @ApiModelProperty(value = "金豆")
    private Integer integral;

    @ApiModelProperty(value = "经验")
    private Integer experience;




}
