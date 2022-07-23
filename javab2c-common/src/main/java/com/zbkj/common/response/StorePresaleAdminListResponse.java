package com.zbkj.common.response;

import com.zbkj.common.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 预售列表响应体
 */
@Data
public class StorePresaleAdminListResponse {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "预售ID")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "预售总人数")
    private Integer people;

    @ApiModelProperty(value = "开始时间")
    private String addTime;

    @ApiModelProperty(value = "结束时间")
    private String stopTime;

    @ApiModelProperty(value = "团长id 0为团长")
    private Integer kId;

    @ApiModelProperty(value = "状态1进行中2已完成3未完成")
    private Integer status;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "几人参团")
    private Integer countPeople;

    @ApiModelProperty(value = "指定中奖用户信息")
    private User winnerUser;

    @ApiModelProperty(value = "中奖时间")
    private Long winnerTime;

    @ApiModelProperty(value = "预售商品")
    private String title;

    @ApiModelProperty(value = "自动开团cron表达式")
    private String autoSystemCron;

    @ApiModelProperty(value = "是否系统自动开团")
    private Boolean autoSystem;

}
