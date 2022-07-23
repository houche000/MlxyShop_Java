package com.zbkj.common.constants;

/**
 * 支付宝配置
 */
public class YyPayConfig {

    public static final String BUSINESS_NUMBER = "tripartite_mchid";
    // 商户appid
    public static final String APPID = "tripartite_appid";

    // 私钥 pkcs8格式的
    public static final String APP_SECRET = "tripartite_appsecret";

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static final String NOTIFY_URL = "yy_pay_notify_url";

    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static final String RETURN_URL = "yy_pay_return_url";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static final String REFUND_NOTIFY_URL = "yy_pay_refund_notify_url";

    // 货币代码
    public static final String CURRENCY = "cny";

    // 请求网关地址
    public static String URL = "https://pay.uipay.cn";
}
