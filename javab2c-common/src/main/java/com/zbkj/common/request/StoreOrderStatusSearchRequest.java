package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单操作记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreOrderStatusSearchRequest对象", description="订单操作记录公共查询对象")
public class StoreOrderStatusSearchRequest {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;
}
