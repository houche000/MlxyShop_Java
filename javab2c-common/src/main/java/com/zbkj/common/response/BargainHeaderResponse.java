package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;

/**
 * 砍价商品列表header响应对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BargainHeaderResponse对象", description="砍价商品列表header响应对象")
public class BargainHeaderResponse {

    @ApiModelProperty(value = "参与砍价总人数")
    private Integer bargainTotal;

    @ApiModelProperty(value = "砍价成功列表（默认7条）")
    private List<HashMap<String, Object>> bargainSuccessList;

}
