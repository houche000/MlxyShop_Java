package com.zbkj.common.response;

import lombok.Data;

import java.util.Map;

@Data
public class OcpayWithdrawResponse {
    private Boolean status;
    private Map<String,Object> returnData;
}
