package com.zbkj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.admin.model.ScheduleJob;

import java.util.List;

/**
 * ScheduleJobService 接口
 */
public interface ScheduleJobService extends IService<ScheduleJob> {

    /**
     * 自动删除日志
     */
    void autoDeleteLog();

    /**
     * 根据拼团商品id查询定时任务
     *
     * @param combinationId
     * @return
     */
    ScheduleJob getByCombinationId(Integer combinationId);

    /**
     * 更改定时任务状态
     *
     * @param scheduleJob /
     */
    void updateScheduleJob(ScheduleJob scheduleJob);

    /**
     * 删除定时任务
     *
     * @param scheduleJob /
     */
    void deleteScheduleJob(ScheduleJob scheduleJob);

    /**
     * 查询启用的任务
     *
     * @return List
     */
    List<ScheduleJob> findByIsPauseIsFalse(Integer status);
}