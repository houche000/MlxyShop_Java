package com.zbkj.common.enums;

/**
 * 方法类型枚举
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public enum MethodType {

    ADD("add", "增加"),
    DELETE("delete", "删除"),
    UPDATE("update", "修改"),
    EXPORT("export", "导出"),
    SELECT("query", "查询");

    private final String type;
    private final String name;

    MethodType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getName(String type){
        for (MethodType modelType : MethodType.values()) {
            if (modelType.getType().equals(type))
                return modelType.getName();
        }
        return null;
    }

    public static String getType(String name){
        for (MethodType modelType : MethodType.values()) {
            if (modelType.getName().equals(name))
                return modelType.getType();
        }
        return "-1";
    }
}
