package com.zbkj.service.service;

import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.vo.MyRecord;

import java.util.HashMap;

/**
 * 页面布局接口
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PageLayoutService {

    /**
     * 页面首页
     * @return 首页信息
     */
    HashMap<String, Object> index();

    /**
     * 首页保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean save(JSONObject jsonObject);

    /**
     * 页面首页banner保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean indexBannerSave(JSONObject jsonObject);

    /**
     * 页面首页menu保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean indexMenuSave(JSONObject jsonObject);

    /**
     * 页面首页新闻保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean indexNewsSave(JSONObject jsonObject);

    /**
     * 页面用户中心banner保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean userBannerSave(JSONObject jsonObject);

    /**
     * 页面用户中心导航保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean userMenuSave(JSONObject jsonObject);

    /**
     * 页面用户中心商品table保存
     * @param jsonObject 数据
     * @return Boolean
     */
    Boolean indexTableSave(JSONObject jsonObject);

    /**
     * 获取分类页配置
     * @return MyRecord
     */
    MyRecord getCategoryConfig();

    /**
     * 分类页配置保存
     * @param jsonObject 配置数据
     * @return Boolean
     */
    Boolean categoryConfigSave(JSONObject jsonObject);
}
