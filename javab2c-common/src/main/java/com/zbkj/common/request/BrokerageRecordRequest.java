package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 资金监控
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BrokerageRecordRequest对象", description = "佣金记录请求对象")
public class BrokerageRecordRequest implements Serializable {

    private static final long serialVersionUID = 3362714265772774491L;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "类型：1-订单返佣，2-申请提现，3-提现失败，4-提现成功，5-佣金转余额")
    @Range(min = 1, max = 5, message = "未知的类型")
    private Integer type;


}
