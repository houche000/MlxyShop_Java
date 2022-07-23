package com.zbkj.common.response;

import com.zbkj.common.model.user.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员详情顶部信息
 */
@Data
public class TopDetail {

    private User user;
    // 余额
    private BigDecimal balance;
    // 金豆
    private Integer integralCount;
    // 总计订单
    private Integer allOrderCount;
    // 本月订单
    private Integer mothOrderCount;
    // 总消费金额
    private BigDecimal allConsumeCount;
    // 本月消费金额
    private BigDecimal mothConsumeCount;
}
