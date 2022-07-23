package com.zbkj.common.constants;

/**
 *  系统设置常量类
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public class SysConfigConstants {

    //后台首页登录图片
    /**
     * 登录页LOGO
     */
    public static final String CONFIG_KEY_ADMIN_LOGIN_LOGO_LEFT_TOP = "site_logo_lefttop";
    public static final String CONFIG_KEY_ADMIN_LOGIN_LOGO_LOGIN = "site_logo_login";
    /**
     * 登录页背景图
     */
    public static final String CONFIG_KEY_ADMIN_LOGIN_BACKGROUND_IMAGE = "admin_login_bg_pic";

    /**
     * 微信分享图片（公众号）
     */
    public static final String CONFIG_KEY_ADMIN_WECHAT_SHARE_IMAGE = "wechat_share_img";
    /**
     * 微信分享标题（公众号）
     */
    public static final String CONFIG_KEY_ADMIN_WECHAT_SHARE_TITLE = "wechat_share_title";
    /**
     * 微信分享简介（公众号）
     */
    public static final String CONFIG_KEY_ADMIN_WECHAT_SHARE_SYNOSIS = "wechat_share_synopsis";


    /**
     * 是否启用分销
     */
    public static final String CONFIG_KEY_BROKERAGE_FUNC_STATUS = "brokerage_func_status";
    /**
     * 分销模式 :1-指定分销，2-人人分销
     */
    public static final String CONFIG_KEY_STORE_BROKERAGE_STATUS = "store_brokerage_status";
    /**
     * 分销模式 :1-指定分销
     */
    public static final String STORE_BROKERAGE_STATUS_APPOINT = "1";
    /**
     * 分销模式 :2-人人分销
     */
    public static final String STORE_BROKERAGE_STATUS_PEOPLE = "2";
    /**
     * 一级返佣比例
     */
    public static final String CONFIG_KEY_STORE_BROKERAGE_RATIO = "store_brokerage_ratio";
    /**
     * 二级返佣比例
     */
    public static final String CONFIG_KEY_STORE_BROKERAGE_TWO = "store_brokerage_two";
    /**
     * 判断是否开启气泡
     */
    public static final String CONFIG_KEY_STORE_BROKERAGE_IS_BUBBLE = "store_brokerage_is_bubble";
    /**
     * 判断是否分销消费门槛
     */
    public static final String CONFIG_KEY_STORE_BROKERAGE_QUOTA = "store_brokerage_quota";

    /**
     * 是否开启会员功能
     */
    public static final String CONFIG_KEY_VIP_OPEN = "vip_open";
    /**
     * 是否开启充值功能
     */
    public static final String CONFIG_KEY_RECHARGE_SWITCH = "recharge_switch";
    /**
     * 是否开启门店自提
     */
    public static final String CONFIG_KEY_STORE_SELF_MENTION = "store_self_mention";
    /**
     * 腾讯地图key
     */
    public static final String CONFIG_SITE_TENG_XUN_MAP_KEY = "tengxun_map_key";
    /**
     * 退款理由
     */
    public static final String CONFIG_KEY_STOR_REASON = "stor_reason";
    /**
     * 提现最低金额
     */
    public static final String CONFIG_EXTRACT_MIN_PRICE = "user_extract_min_price";
    /**
     * 提现冻结时间
     */
    public static final String CONFIG_EXTRACT_FREEZING_TIME = "extract_time";

    /**
     * 全场满额包邮开关
     */
    public static final String STORE_FEE_POSTAGE_SWITCH = "store_free_postage_switch";
    /**
     * 全场满额包邮金额
     */
    public static final String STORE_FEE_POSTAGE = "store_free_postage";
    /**
     * 金豆抵用比例(1金豆抵多少金额)
     */
    public static final String CONFIG_KEY_INTEGRAL_RATE = "integral_ratio";
    /**
     * 下单支付金额按比例赠送金豆（实际支付1元赠送多少金豆)
     */
    public static final String CONFIG_KEY_INTEGRAL_RATE_ORDER_GIVE = "order_give_integral";

    /**
     * 微信支付开关
     */
    public static final String CONFIG_PAY_WEIXIN_OPEN = "pay_weixin_open";
    /**
     * 余额支付状态
     */
    public static final String CONFIG_YUE_PAY_STATUS = "yue_pay_status";
    /**
     * 支付宝支付状态
     */
    public static final String CONFIG_ALI_PAY_STATUS = "ali_pay_status";

    /**
     * kab支付状态
     */
    public static final String CONFIG_KAB_PAY_STATUS = "kab_pay_status";
    /**
     * 盈富支付状态
     */
    public static final String CONFIG_YY_PAY_STATUS = "yy_pay_status";
    /**
     * 雷蛇支付状态
     */
    public static final String CONFIG_RAZER_PAY_STATUS = "razer_pay_status";

    public static final String CONFIG_DURIAN_PAY_STATUS = "durian_pay_status";

    public static final String CONFIG_ADA_PAY_STATUS = "ada_pay_status";
    /**
     * 用户可以每天发起拼团数
     */
    public static final String DAILY_GROUP_START_MAX = "daily_group_start_max";
    /**
     * 个人每天参与秒杀次数
     */
    public static final String MAX_SECKIL_NUMBER = "max_seckil_number";
    /**
     * 全局默认退款类型 0-原路返回
     * 1-退款到余额
     */
    public static final String REFUND_CONFIG_TYPE = "refundConfig_type";
    /**
     * 用户可以每天参与拼团数
     */
    public static final String DAILY_GROUP_MAX = "daily_group_max";
}
