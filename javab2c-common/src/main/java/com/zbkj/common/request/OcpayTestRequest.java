package com.zbkj.common.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OcpayTestRequest {

    private BigDecimal extractPrice;
    private String bankName;
    private String bankAccountNum;
    private String bankAccountName;
}
