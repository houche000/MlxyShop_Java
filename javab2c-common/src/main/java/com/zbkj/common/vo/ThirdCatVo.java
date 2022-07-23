package com.zbkj.common.vo;

import lombok.Data;

/**
 * 第三级类目
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class ThirdCatVo {

    /** 类目ID */
    private Integer thirdCatId;

    /** 类目名称 */
    private String thirdCatName;

    /** 类目资质 */
    private String qualification;

    /** 类目资质类型,0:不需要,1:必填,2:选填 */
    private Integer qualificationType;

    /** 商品资质 */
    private String productQualification;

    /** 商品资质类型,0:不需要,1:必填,2:选填 */
    private Integer productQualificationType;

}
