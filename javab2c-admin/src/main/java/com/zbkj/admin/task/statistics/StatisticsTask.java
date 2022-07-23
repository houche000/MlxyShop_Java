package com.zbkj.admin.task.statistics;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.StatisticsTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 统计定时任务（每天零点）
 */
@Component("StatisticsTask")
public class StatisticsTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(StatisticsTask.class);

    @Autowired
    private StatisticsTaskService statisticsTaskService;

    /**
     * 每天1点执行
     */
    public void statistics() {
        // cron : 0 0 0 */1 * ?
        logger.info("---StatisticsTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            statisticsTaskService.autoStatistics();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StatisticsTask.task" + " | msg : " + e.getMessage());
        }
    }

}
