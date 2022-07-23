package com.zbkj.service.service;

/**
 * 微信公众号service
 */
public interface WechatPublicService {

    /**
     * 获取公众号自定义菜单
     */
    Object getCustomizeMenus();

    /**
     * 保存自定义菜单
     * @param data 菜单json
     * @return Boolean
     */
    Boolean createMenus(String data);

    /**
     * 删除自定义菜单
     * @return Boolean
     */
    Boolean deleteMenus();
}
