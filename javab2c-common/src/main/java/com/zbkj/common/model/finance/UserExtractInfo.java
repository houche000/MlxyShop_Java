package com.zbkj.common.model.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/3/7 16:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_user_extract_info")
@ApiModel(value = "UserExtractInfo对象", description = "用户提现账号信息表")
public class UserExtractInfo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    @ApiModelProperty(value = "提现账号名称/姓名")
    private String realName;

    @ApiModelProperty(value = "bank = 银行卡 alipay = 支付宝 weixin=微信")
    private String extractType;

    @ApiModelProperty(value = "提现账号id,银行卡号")
    private String accountName;

    @ApiModelProperty(value = "身份证号")
    private String icNumber;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行卡图片")
    private String bankImage;

    @ApiModelProperty(value = "开户地址/二维码")
    private String address;
}
