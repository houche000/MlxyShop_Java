package com.zbkj.admin.task.log;

import com.zbkj.admin.service.ScheduleJobLogService;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.WechatExceptionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动删除不需要的历史日志
 */
@Component("AutoDeleteLogTask")
public class AutoDeleteLogTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(AutoDeleteLogTask.class);

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;
    @Autowired
    private WechatExceptionsService wechatExceptionsService;

    /**
     * 每天0点执行scheduleJobService
     */
    public void autoDeleteLog() {
        // cron : 0 0 0 */1 * ?
        logger.info("---BargainStopChangeTask------bargain stop status change task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            scheduleJobLogService.autoDeleteLog();
            wechatExceptionsService.autoDeleteLog();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BargainStopChangeTask" + " | msg : " + e.getMessage());
        }
    }

}
