package com.zbkj.common.vo;

import lombok.Data;

import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Data
public class CatItem {

    private Integer value;

    private String label;

    private String qualification;

    private Integer qualificationType;

    private String productQualification;

    private Integer productQualificationType;

    private List<CatItem> children;

    public CatItem() {
    }

    public CatItem(Integer value, String label, List<CatItem> childrens) {
        this.value = value;
        this.label = label;
        this.children = childrens;
    }
}
