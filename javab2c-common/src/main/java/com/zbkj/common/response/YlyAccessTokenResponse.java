package com.zbkj.common.response;

import lombok.Data;

/**
 * 易联云 获取AccessToken response
 **/
@Data
public class YlyAccessTokenResponse {
    private String error;
    private String error_description;
    private YlyAccessTokenBodyResponse body;
}
