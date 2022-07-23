package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 充值套餐响应对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserRechargeItemResponse对象", description="充值套餐响应对象")
public class UserRechargeItemResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "充值模板id")
    private Integer id;

    @ApiModelProperty(value = "充值金额")
    private String price;

    @ApiModelProperty(value = "赠送金额")
    private String giveMoney;

}
