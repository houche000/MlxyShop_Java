package com.zbkj.admin.task.integral;


import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.UserIntegralRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 金豆冻结期解冻task
 */
@Component("IntegralFrozenTask")
public class IntegralFrozenTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(IntegralFrozenTask.class);

    @Autowired
    private UserIntegralRecordService userIntegralRecordService;

    /**
     * 1分钟同步一次数据
     */
    public void integralFrozen() {
        // cron : 0 */1 * * * ?
        logger.info("---IntegralFrozenTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            userIntegralRecordService.integralThaw();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("IntegralFrozenTask.task" + " | msg : " + e.getMessage());
        }
    }

}
