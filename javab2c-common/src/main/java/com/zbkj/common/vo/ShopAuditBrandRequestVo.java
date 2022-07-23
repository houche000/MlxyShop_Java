package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 上传品牌信息 request
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopAuditBrandRequestVo {

    /** 上传品牌信息参数对象 */
    @TableField(value = "audit_req")
    private ShopAuditBrandRequestItemVo auditReq;
}
