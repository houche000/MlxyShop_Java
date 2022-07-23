package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.log.SensitiveMethodLog;
import com.zbkj.common.request.PageParamRequest;

/**
 * SensitiveMethoyLogService 接口
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface SensitiveMethodLogService extends IService<SensitiveMethodLog> {

    /**
     * 添加敏感记录
     * @param methodLog 记录信息
     */
    void addLog(SensitiveMethodLog methodLog);

    /**
     * 分页列表
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<SensitiveMethodLog> getPageList(PageParamRequest pageParamRequest);
}