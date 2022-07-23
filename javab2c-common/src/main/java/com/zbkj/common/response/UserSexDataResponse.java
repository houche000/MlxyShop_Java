package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户性别数据对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserSexDataResponse对象", description="用户性别数据对象")
public class UserSexDataResponse implements Serializable {

    private static final long serialVersionUID = -6004724917253583732L;

    @ApiModelProperty(value = "性别,0未知，1男，2女，3保密")
    private Integer sex;

    @ApiModelProperty(value = "数量")
    private Integer num;
}
