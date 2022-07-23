package com.zbkj.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 自定义交易组件商品列表请求Vo
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ShopSpuListResponseVo {

    /** 总数 */
    @TableField(value = "total_num")
    private Integer totalNum;

    /** spu数组 */
    private List<ShopSpuVo> spus;
}
