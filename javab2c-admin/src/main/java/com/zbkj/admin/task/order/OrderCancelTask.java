package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户取消订单task任务
 */
@Component("OrderCancelTask")
public class OrderCancelTask {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 1分钟同步一次数据
     */
    public void userCancel() {
        // cron : 0 */1 * * * ?
        logger.info("---OrderCancelTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.cancelByUser();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderCancelTask.task" + " | msg : " + e.getMessage());
        }
    }
}
