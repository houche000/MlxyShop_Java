package com.zbkj.common.vo;

import lombok.Data;

/**
 * RegisterCheckAccessInfoItemVo 获取接入状态Response
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class RegisterCheckDataItemnVo {
    // 审核状态, 2: 已接入, 3: 封禁中
    private Integer status;
    private RegisterCheckAccessInfoItemVo access_info;
}
