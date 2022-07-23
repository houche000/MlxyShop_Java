package com.zbkj.common.enums;

/**
 * @author: zhongyehai
 * @description: 提现类型
 * @date: 2022/3/5 15:56
 */
public enum EnumFundType {
    /**
     * 余额
     */
    BALANCE("余额", 1),
    /**
     * 佣金
     */
    COMMISSION("佣金", 2);

    private final String name;
    private final int code;

    EnumFundType(String name, int i) {
        this.name = name;
        this.code = i;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
