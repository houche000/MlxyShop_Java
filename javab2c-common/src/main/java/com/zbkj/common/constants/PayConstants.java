package com.zbkj.common.constants;

/**
 *  支付相关常量类
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 *  +----------------------------------------------------------------------
 */
public class PayConstants {

    //支付方式
    public static final String PAY_TYPE_WE_CHAT = "weixin"; //微信支付
    public static final String PAY_TYPE_YUE = "yue"; //余额支付
    public static final String PAY_TYPE_YY_PAY = "yypay"; //盈富支付
    public static final String PAY_TYPE_RAZER_PAY = "razer"; //雷蛇支付

    public static final String PAY_TYPE_KAB_PAY = "kabpay"; //kab支付
    public static final String PAY_TYPE_SAND_PAY = "sandpay"; //杉德支付
    public static final String PAY_TYPE_ADA_PAY = "adapay"; //ada支付
    public static final String PAY_TYPE_OFFLINE = "offline"; //线下支付
    public static final String PAY_TYPE_ALI_PAY = "alipay"; //支付宝
    public static final String PAY_TYPE_DURIAN_PAY = "durianpay";
    public static final String PAY_TYPE_ZERO_PAY = "zeroPay"; // 零元付

    //支付渠道
    public static final String PAY_CHANNEL_WE_CHAT_H5 = "weixinh5"; //H5唤起微信支付
    public static final String PAY_CHANNEL_WE_CHAT_PUBLIC = "public"; //公众号
    public static final String PAY_CHANNEL_WE_CHAT_PROGRAM = "routine"; //小程序
    public static final String PAY_CHANNEL_WE_CHAT_APP_IOS = "weixinAppIos"; //微信App支付ios
    public static final String PAY_CHANNEL_WE_CHAT_APP_ANDROID = "weixinAppAndroid"; //微信App支付android
    public static final String PAY_CHANNEL_QR_CASHIER = "QR_CASHIER"; //聚合扫码(用户扫商家)
    public static final String PAY_CHANNEL_AUTO_BAR = "AUTO_BAR"; //聚合条码(商家扫用户)
    public static final String PAY_CHANNEL_ALI_BAR = "ALI_BAR"; //支付宝条码
    public static final String PAY_CHANNEL_ALI_JSAPI = "ALI_JSAPI"; //支付宝生活号
    public static final String PAY_CHANNEL_ALI_APP = "ALI_APP"; //支付宝APP
    public static final String PAY_CHANNEL_ALI_WAP = "ALI_WAP"; //支付宝WAP
    public static final String PAY_CHANNEL_ALI_PC = "ALI_PC"; //支付宝PC网站
    public static final String PAY_CHANNEL_ALI_QR = "ALI_QR"; //支付宝二维码
    public static final String PAY_CHANNEL_WX_BAR = "WX_BAR"; //微信条码
    public static final String PAY_CHANNEL_WX_JSAPI = "WX_JSAPI"; //微信公众号
    public static final String PAY_CHANNEL_WX_LITE = "WX_LITE"; //微信小程序
    public static final String PAY_CHANNEL_WX_APP = "WX_APP"; //微信APP
    public static final String PAY_CHANNEL_WX_H5 = "WX_H5"; //微信H5
    public static final String PAY_CHANNEL_WX_NATIVE = "WX_NATIVE"; //微信扫码
    public static final String PAY_CHANNEL_YSF_BAR = "YSF_BAR"; //云闪付条码
    public static final String PAY_CHANNEL_YSF_JSAPI = "YSF_JSAPI"; //云闪付jsapi

    public static final String PAY_CHANNEL_ALI_PAY = "alipay"; //支付宝支付
    public static final String PAY_CHANNEL_ALI_APP_PAY = "appAliPay"; //支付宝App支付

    public static final String PAY_CHANNEL_ADA_ALI_PAY = "alipay"; //支付宝App支付
    public static final String PAY_CHANNEL_ADA_ALI_WAP_PAY = "alipay_wap"; //支付宝 H5 支付
    public static final String PAY_CHANNEL_ADA_WX_PUB_PAY = "wx_pub"; //微信公众号支付
    public static final String PAY_CHANNEL_ADA_WX_LITE_PAY = "wx_lite"; //微信小程序支付

    public static final String WX_PAY_TRADE_TYPE_JS = "JSAPI";
    public static final String WX_PAY_TRADE_TYPE_H5 = "MWEB";

    //微信支付接口请求地址
    public static final String WX_PAY_API_URL = "https://api.mch.weixin.qq.com/";
    // 微信统一预下单
    public static final String WX_PAY_API_URI = "pay/unifiedorder";
    // 微信查询订单
    public static final String WX_PAY_ORDER_QUERY_API_URI = "pay/orderquery";
    // 微信支付回调地址
    public static final String WX_PAY_NOTIFY_API_URI = "/api/admin/payment/callback/wechat";
    // 微信退款回调地址
    public static final String WX_PAY_REFUND_NOTIFY_API_URI = "/api/admin/payment/callback/wechat/refund";

    public static final String WX_PAY_SIGN_TYPE_MD5 = "MD5";
    public static final String WX_PAY_SIGN_TYPE_SHA256 = "HMAC-SHA256";

    public static final String PAY_BODY = "支付中心-订单支付";
    public static final String FIELD_SIGN = "sign";

    // 公共号退款
    public static final String WX_PAY_REFUND_API_URI= "secapi/pay/refund";
}
