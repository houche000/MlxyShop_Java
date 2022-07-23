package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户渠道数据对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserChannelDataResponse对象", description="用户渠道数据对象")
public class UserChannelDataResponse implements Serializable {

    private static final long serialVersionUID = -6004724917253583732L;

    @ApiModelProperty(value = "性别,h5,ios,routine,wechat")
    private String channel;

    @ApiModelProperty(value = "数量")
    private Integer num;
}
