package com.zbkj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.admin.model.ScheduleJobLog;

/**
* @author HZW
* @description ScheduleJobLogService 接口
* @date 2021-11-30
*/
public interface ScheduleJobLogService extends IService<ScheduleJobLog> {
    /**
     * 自动删除日志
     */
    void autoDeleteLog();
}
