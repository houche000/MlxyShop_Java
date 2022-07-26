package com.zbkj.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *  快递轨迹
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LogisticsResultVo对象", description="快递接口返回数据")
public class LogisticsResultVo {

    @ApiModelProperty(value = "快递单号")
    private String no;

    @ApiModelProperty(value = "快递简写")
    private String com;

    @ApiModelProperty(value = "快递运送轨迹")
    private List<LogisticsResultListVo> list;

    @ApiModelProperty(value = "快递收件(揽件)-1.单号或快递公司代码错误 0.暂无轨迹 1.在途中 2.正在派件 3.已签收 4.派送失败 5.疑难件 6.退件签收 */")
    @JsonProperty("State")
    private String state;

    @ApiModelProperty(value = "是否签收")
    @JsonProperty("issign")
    private String isSign;

    @ApiModelProperty(value = "快递公司名称")
    private String cname;

    @ApiModelProperty(value = "快递公司官网")
    private String Site;

    @ApiModelProperty(value = "快递公司电话")
    private String Phone;

    @ApiModelProperty(value = "快递员 或 快递站(没有则为空")
    private String Courier;

    @ApiModelProperty(value = "快递员电话 (没有则为空)")
    private String CourierPhone;

    @ApiModelProperty(value = "快递轨迹信息最新时间 ")
    private String updateTime;

    @ApiModelProperty(value = "发货到收货消耗时长 (截止最新轨迹)")
    private String takeTime;

    @ApiModelProperty(value = "快递公司LOGO")
    private String Logo;

}
