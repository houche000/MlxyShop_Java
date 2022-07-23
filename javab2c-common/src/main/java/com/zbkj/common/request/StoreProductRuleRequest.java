package com.zbkj.common.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 商品规则值(规格)表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_product_rule")
@ApiModel(value="StoreProductRuleRequest对象", description="商品规则值(规格)表")
public class StoreProductRuleRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "规则id")
    private Integer id;

    @ApiModelProperty(value = "规格名称")
    @NotBlank(message = "规格名称不能为空")
    @Length(max = 32, message = "规格名称长度不能超过32个字符")
    private String ruleName;

    @ApiModelProperty(value = "规格值【JSON字符串】 [{\\\"detail\\\": [\\\"string\\\"],\\\"title\\\": \\\"string\\\"}]")
    @NotBlank(message = "规格值不能为空")
    private String ruleValue;
}
