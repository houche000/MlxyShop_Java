package com.zbkj.common.response;

import com.zbkj.common.model.product.StoreProductAttr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 预售商品响应体
 */
@Data
public class PresaleDetailResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "预售商品信息")
    private PresaleDetailH5Response storePresale;

    @ApiModelProperty(value = "商品规格")
    private List<StoreProductAttr> productAttr;

    @ApiModelProperty(value = "商品规格值")
    private HashMap<String,Object> productValue;

    @ApiModelProperty(value = "主商品状态:normal-正常，sellOut-售罄，soldOut-下架,delete-删除")
    private String masterStatus;
}
