package com.zbkj.common.response;

import com.zbkj.common.model.presale.StorePresale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 预售首页响应对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PresaleIndexResponse对象", description="预售首页响应对象")
public class PresaleIndexResponse {

    @ApiModelProperty(value = "预售3位用户头像")
    private List<String> avatarList;

    @ApiModelProperty(value = "预售总人数")
    private Integer totalPeople;

    @ApiModelProperty(value = "预售商品列表")
    private List<StorePresale> productList;

}
