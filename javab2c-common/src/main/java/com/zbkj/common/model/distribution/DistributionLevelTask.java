package com.zbkj.common.model.distribution;

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
 * 分销等级表
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_distribution_level_task")
@ApiModel(value="DistributionLevelTask", description="分销等级任务表")
public class DistributionLevelTask implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分销等级任务id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "等级ID")
    private Integer levelId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "任务类型")
    private Integer type;

    @ApiModelProperty(value = "任务数量")
    private Integer number;

    @ApiModelProperty(value = "状态 是否可用 ")
    private Boolean status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
