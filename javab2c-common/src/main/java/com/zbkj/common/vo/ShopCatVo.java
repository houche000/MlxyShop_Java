package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 *  商品类型Vo对象
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopCatVo {

    /** 错误码 */
    @TableField(value = "errcode")
    private Integer errCode;

    /** 错误信息 */
    @TableField(value = "errmsg")
    private Integer errMsg;

    /** 类目列表 */
    @TableField(value = "third_cat_list")
    private List<ShopCatDetailVo> thirdCatList;
}
