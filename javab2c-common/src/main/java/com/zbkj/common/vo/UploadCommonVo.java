package com.zbkj.common.vo;

import lombok.Data;

/**
 * 文件公共上传对象
 *
 */
@Data
public class UploadCommonVo {

    //服务器存储地址
    private String rootPath;

    //类型
    private String type;

    //模块
    private String modelPath;

    //扩展名
    private String extStr;

    //文件大小上限
    private int size;

}
