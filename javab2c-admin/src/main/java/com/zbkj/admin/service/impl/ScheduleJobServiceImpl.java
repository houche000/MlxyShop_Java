package com.zbkj.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.admin.dao.ScheduleJobDao;
import com.zbkj.admin.model.ScheduleJob;
import com.zbkj.admin.quartz.ScheduleManager;
import com.zbkj.admin.service.ScheduleJobService;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.service.service.StoreCombinationService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
*  ScheduleJobServiceImpl 接口实现
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJob> implements ScheduleJobService {

    @Value("${crmeb.scheduler.enable}")
    private boolean enable;

    @Resource
    private ScheduleJobDao dao;

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private StoreCombinationService storeCombinationService;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        if (enable) {
            // 同步所有拼团定时任务
            LambdaQueryWrapper<StoreCombination> combinationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            combinationLambdaQueryWrapper.isNotNull(StoreCombination::getAutoSystemCron)
                    .eq(StoreCombination::getAutoSystem, true)
                    .eq(StoreCombination::getIsShow, true);
            List<StoreCombination> combinations = storeCombinationService.list(combinationLambdaQueryWrapper);
            Map<String, ScheduleJob> allScheduleJobMap = combinations.stream().map(e -> {
                ScheduleJob scheduleJob = new ScheduleJob();
                scheduleJob.setCombinationId(e.getId());
                scheduleJob.setBeanName("AutoStartPinkTask");
                scheduleJob.setMethodName("startBotPink");
                scheduleJob.setParams(e.getId().toString());
                scheduleJob.setCronExpression(e.getAutoSystemCron());
                if (e.getAutoSystem() && e.getIsShow()) {
                    scheduleJob.setStatus(0);
                } else {
                    scheduleJob.setStatus(1);
                }
                scheduleJob.setCreateTime(new Date());
                return scheduleJob;
            }).collect(Collectors.toMap(ScheduleJob::getParams, Function.identity(), (e1, e2) -> e1));

            LambdaQueryWrapper<ScheduleJob> scheduleJobQueryWrapper = new LambdaQueryWrapper<>();
            scheduleJobQueryWrapper.eq(ScheduleJob::getBeanName, "AutoStartPinkTask");
            List<ScheduleJob> scheduleJobs = this.list(scheduleJobQueryWrapper);
            Map<String, ScheduleJob> localScheduleJob = scheduleJobs.stream().collect(Collectors.toMap(ScheduleJob::getParams, Function.identity(), (e1, e2) -> e1));
            List<ScheduleJob> updateList = localScheduleJob.keySet().stream().filter(allScheduleJobMap::containsKey).map(key -> {
                ScheduleJob scheduleJob = localScheduleJob.get(key);
                scheduleJob.setParams(allScheduleJobMap.get(key).getParams());
                scheduleJob.setCronExpression(allScheduleJobMap.get(key).getCronExpression());
                scheduleJob.setStatus(allScheduleJobMap.get(key).getStatus());
                return scheduleJob;
            }).collect(Collectors.toList());
            List<Integer> deleteIds = localScheduleJob.keySet().stream().filter(key -> !allScheduleJobMap.containsKey(key)).map(key -> localScheduleJob.get(key).getJobId()).collect(Collectors.toList());
            List<ScheduleJob> insertList = allScheduleJobMap.keySet().stream().filter(key -> !localScheduleJob.containsKey(key)).map(allScheduleJobMap::get).collect(Collectors.toList());
            // 删除数据库不存在拼团的定时任务
            if (CollUtil.isNotEmpty(deleteIds)) {
                this.removeByIds(deleteIds);
            }
            // 同步新增定时任务
            if (CollUtil.isNotEmpty(insertList)) {
                this.saveBatch(insertList);
            }
            // 同步更新定时任务
            if (CollUtil.isNotEmpty(updateList)) {
                this.updateBatchById(updateList);
            }

            List<ScheduleJob> scheduleJobList = getAll();
            try {
                List<Integer> allJobIds = scheduleManager.getAllJobIds();
                // 删除所有job
                if (CollUtil.isNotEmpty(allJobIds)) {
                    for (Integer jobId : allJobIds) {
                        scheduleManager.deleteScheduleJobById(jobId);
                    }
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            scheduleJobList.forEach(scheduleJob -> {
                // 如果定时任务不存在，则创建定时任务
                scheduleManager.updateScheduleJob(scheduleJob);
            });
        }
    }

    /**
     * 获取所有的job
     * @return List<ScheduleJob>
     */
    private List<ScheduleJob> getAll() {
        LambdaQueryWrapper<ScheduleJob> lqw = Wrappers.lambdaQuery();
        lqw.eq(ScheduleJob::getIsDelte, false);
        return dao.selectList(lqw);
    }

    /**
     * 根据拼团商品id查询定时任务
     *
     * @param combinationId
     * @return
     */
    @Override
    public ScheduleJob getByCombinationId(Integer combinationId) {
        LambdaQueryWrapper<ScheduleJob> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ScheduleJob::getCombinationId, combinationId);
        return this.getOne(queryWrapper);
    }

    /**
     * 自动删除日志
     * 只保留十天的日志
     */
    @Override
    public void autoDeleteLog() {
        String beforeDate = DateUtil.offsetDay(new Date(), -9).toString("yyyy-MM-dd");
        UpdateWrapper<ScheduleJob> wrapper = Wrappers.update();
        wrapper.lt("create_time", beforeDate);
        dao.delete(wrapper);
    }

    /**
     * 更改定时任务
     *
     * @param scheduleJob /
     */
    @Override
    public void updateScheduleJob(ScheduleJob scheduleJob) {
        this.saveOrUpdate(scheduleJob);
        scheduleManager.updateScheduleJob(scheduleJob);
    }

    /**
     * 立即执行定时任务
     *
     * @param scheduleJob /
     */
    @Override
    public void deleteScheduleJob(ScheduleJob scheduleJob) {
        scheduleManager.deleteScheduleJob(scheduleJob);
    }

    /**
     * 查询所有指定状态的任务
     *
     * @return List
     */
    @Override
    public List<ScheduleJob> findByIsPauseIsFalse(Integer status) {
        LambdaQueryWrapper<ScheduleJob> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ScheduleJob::getStatus, status);
        return this.list(queryWrapper);

    }
}

