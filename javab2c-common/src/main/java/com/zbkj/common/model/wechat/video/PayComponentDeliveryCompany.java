package com.zbkj.common.model.wechat.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 组件快递公司表
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_pay_component_delivery_company")
@ApiModel(value="PayComponentDeliveryCompany对象", description="组件快递公司表")
public class PayComponentDeliveryCompany implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "快递公司id")
    private String deliveryId;

    @ApiModelProperty(value = "快递公司名称")
    private String deliveryName;

    @ApiModelProperty(value = "是否删除 0未删除 1删除")
    private Boolean isDel;


}
