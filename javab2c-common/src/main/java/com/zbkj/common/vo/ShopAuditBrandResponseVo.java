package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopAuditBrandResponseVo extends BaseResultResponseVo {
    // 审核单id
    @TableField(value = "audit_id")
    private String auditId;
}
