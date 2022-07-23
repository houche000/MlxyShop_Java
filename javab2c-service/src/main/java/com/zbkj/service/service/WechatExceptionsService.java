package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.WechatExceptions;

/**
 *  微信异常服务类
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface WechatExceptionsService extends IService<WechatExceptions> {

    /**
     * 自动删除日志
     */
    void autoDeleteLog();
}