package com.zbkj.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.admin.dao.ScheduleJobLogDao;
import com.zbkj.admin.model.ScheduleJobLog;
import com.zbkj.admin.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author HZW
* @description ScheduleJobLogServiceImpl 接口实现
* @date 2021-11-30
*/
@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLog> implements ScheduleJobLogService {

    @Resource
    private ScheduleJobLogDao dao;

    @Override
    public void autoDeleteLog() {
        String beforeDate = DateUtil.offsetDay(new Date(), -9).toString("yyyy-MM-dd");

        UpdateWrapper<ScheduleJobLog> wrapper = Wrappers.update();

        wrapper.lt("create_time", beforeDate);

        dao.delete(wrapper);
    }
}

