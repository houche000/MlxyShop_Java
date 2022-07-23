package com.zbkj.common.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_user_feed_back")
@ApiModel(value = "Feedback对象", description = "用户意见反馈表")
public class UserFeedback {

    @ApiModelProperty(value = "意见反馈id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户uid")
    private Integer uid;

    @ApiModelProperty(value = "意见内容")
    private String content;

    @ApiModelProperty("意见图片")
    private String img;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("处理结果")
    private String result;

    @ApiModelProperty("当前状态 0 待处理,1 已处理")
    private Integer status;

    @JsonProperty(value = "add_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "处理时间")
    private Date resultTime;


}
