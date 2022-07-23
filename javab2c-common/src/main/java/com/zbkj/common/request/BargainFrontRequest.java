package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * H5 砍价公共请求对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BargainFrontRequest对象", description="砍价公共请求对象")
public class BargainFrontRequest {

    @ApiModelProperty(value = "砍价商品ID", required = true)
    @NotNull(message = "砍价商品编号不能为空")
    private Integer bargainId;

    @ApiModelProperty(value = "用户砍价活动ID")
    private Integer bargainUserId;

    @ApiModelProperty(value = "用户砍价活动Uid")
    private Integer bargainUserUid;
}
