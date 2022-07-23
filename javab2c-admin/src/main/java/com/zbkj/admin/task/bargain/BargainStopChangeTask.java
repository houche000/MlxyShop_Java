package com.zbkj.admin.task.bargain;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.StoreBargainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 砍价活动结束状态变化定时任务
 */
@Component("BargainStopChangeTask")
public class BargainStopChangeTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(BargainStopChangeTask.class);

    @Autowired
    private StoreBargainService storeBargainService;

    /**
     * 每天0点执行
     */
    public void bargainStopChange() {
        // cron : 0 0 0 */1 * ?
        logger.info("---BargainStopChangeTask------bargain stop status change task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            storeBargainService.stopAfterChange();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BargainStopChangeTask" + " | msg : " + e.getMessage());
        }
    }

}
