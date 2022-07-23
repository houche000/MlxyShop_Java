package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户订单完成task任务
 */
@Component("OrderCompleteTask")
public class OrderCompleteTask {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderCompleteTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 1分钟同步一次数据
     */
    public void orderComplete() {
        // cron : 0 */1 * * * ?
        logger.info("---OrderCompleteTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.complete();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderCompleteTask.task" + " | msg : " + e.getMessage());
        }
    }
}
