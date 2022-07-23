package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  秒杀Header时间header响应对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SecKillResponse对象", description="秒杀Header时间header响应对象")
public class SecKillResponse {

    public SecKillResponse() {
    }

    public SecKillResponse(Integer id, String slide, String statusName, String time, int status, String timeSwap) {
        this.id = id;
        this.slide = slide;
        this.statusName = statusName;
        this.time = time;
        this.status = status;
        this.timeSwap = timeSwap;
    }

    @ApiModelProperty(value = "秒杀时段id")
    private Integer id;

    @ApiModelProperty(value = "秒杀时段轮播图")
    private String slide;

    @ApiModelProperty(value = "秒杀时段状态名称")
    private String statusName; // 已结束 抢购中 即将开始

    @ApiModelProperty(value = "秒杀时段状态")
    private int status;

    @ApiModelProperty(value = "秒杀时段时间信息")
    private String time;

    @ApiModelProperty(value = "秒杀时段结束时间")
    private String timeSwap;

    @ApiModelProperty(value = "是否选中")
    private Boolean isCheck = false;
}
