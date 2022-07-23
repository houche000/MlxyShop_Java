package com.zbkj.admin.service.impl;

import com.zbkj.admin.service.ActionService;
import com.zbkj.common.model.log.SensitiveMethodLog;
import com.zbkj.service.service.SensitiveMethodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 行为service实现类
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    private SensitiveMethodLogService sensitiveMethodLogService;

    /**
     * 添加敏感记录
     * @param methodLog 记录信息
     */
    @Async
    @Override
    public void addSensitiveLog(SensitiveMethodLog methodLog) {
        sensitiveMethodLogService.addLog(methodLog);
    }
}
