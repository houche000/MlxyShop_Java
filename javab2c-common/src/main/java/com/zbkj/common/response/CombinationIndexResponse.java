package com.zbkj.common.response;

import com.zbkj.common.model.combination.StoreCombination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 拼团首页响应对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CombinationIndexResponse对象", description="拼团首页响应对象")
public class CombinationIndexResponse {

    @ApiModelProperty(value = "拼团3位用户头像")
    private List<String> avatarList;

    @ApiModelProperty(value = "拼团总人数")
    private Integer totalPeople;

    @ApiModelProperty(value = "拼团商品列表")
    private List<StoreCombination> productList;

}
