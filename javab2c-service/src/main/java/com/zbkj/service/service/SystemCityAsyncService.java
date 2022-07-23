package com.zbkj.service.service;

/**
 * SystemCityAsyncService 接口
 */
public interface SystemCityAsyncService {

    /**
     * 数据整体刷入redis
     */
    void async(Integer id);

    /**
     * 设置属性列表进入redis
     */
    void setListTree();
}
