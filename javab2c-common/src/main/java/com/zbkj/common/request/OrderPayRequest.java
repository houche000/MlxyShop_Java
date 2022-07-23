package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 支付订单参数
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderPayRequest对象", description="订单支付")
public class OrderPayRequest {

    @ApiModelProperty(value = "订单id")
//    @NotNull(message = "订单id不能为空")
    private String uni;

    @ApiModelProperty(value = "订单编号")
    @NotNull(message = "订单编号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "支付类型：weixin-微信支付，yue-余额支付，offline-线下支付，alipay-支付包支付,yypay-富盈YYPay,durianpay,adapay")
    @NotNull(message = "支付类型不能为空")
    private String payType;

    @ApiModelProperty(value = "支付渠道:weixinh5-微信H5支付，public-公众号支付，routine-小程序支付，weixinAppIos-微信appios支付，weixinAppAndroid-微信app安卓支付,alipay-支付包支付，appAliPay-App支付宝支付," +
            "QR_CASHIER聚合扫码(用户扫商家),AUTO_BAR聚合条码(商家扫用户),ALI_BAR支付宝条码,ALI_JSAPI支付宝生活号,ALI_APP支付宝APP,ALI_WAP支付宝WAP,ALI_PC支付宝PC网站,ALI_QR支付宝二维码,WX_BAR微信条码,WX_JSAPI微信公众号,WX_LITE," +
            "WX_APP微信APP,WX_H5微信H5,WX_NATIVE微信扫码,YSF_BAR云闪付条码,YSF_JSAPI云闪付jsapi")
    @NotNull(message = "支付渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "支付平台")
    private String from;

    @ApiModelProperty(value = "下单时小程序的场景值")
    private Integer scene;
}
