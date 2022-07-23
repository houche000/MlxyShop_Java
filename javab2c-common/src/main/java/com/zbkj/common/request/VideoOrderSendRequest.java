package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 视频订单发货对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="VideoOrderSendRequest对象", description="视频订单发货对象")
public class VideoOrderSendRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "快递公司ID")
    @NotBlank(message = "快递公司ID不能为空")
    private String deliveryId;

    @ApiModelProperty(value = "快递单号")
    @NotBlank(message = "快递单号不能为空")
    private String waybillId;
}
