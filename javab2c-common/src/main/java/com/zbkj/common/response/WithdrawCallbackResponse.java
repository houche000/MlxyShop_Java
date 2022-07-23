package com.zbkj.common.response;

import lombok.Data;

import java.util.Map;

@Data
public class WithdrawCallbackResponse {

private Boolean status;
private Map<String,Object> data;
}
