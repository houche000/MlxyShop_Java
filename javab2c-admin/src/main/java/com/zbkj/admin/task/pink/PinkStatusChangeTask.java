package com.zbkj.admin.task.pink;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.StorePinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拼团状态变化Task
 */
@Component("PinkStatusChangeTask")
public class PinkStatusChangeTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(PinkStatusChangeTask.class);

    @Autowired
    private StorePinkService storePinkService;

    /**
     * 每分钟执行一次
     */
    public void pinkStatusChage() {
        // cron : 0 */1 * * * ?
        logger.info("---PinkStatusChange------bargain stop status change task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            storePinkService.detectionStatus();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PinkStatusChange" + " | msg : " + e.getMessage());
        }
    }
}
