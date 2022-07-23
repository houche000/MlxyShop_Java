package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统自动取消未支付订单task任务
 */
@Component("OrderAutoCancelTask")
public class OrderAutoCancelTask {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderAutoCancelTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 1分钟同步一次数据
     */
    public void autoCancel() {
        // cron : 0 */1 * * * ?
        logger.info("---OrderAutoCancelTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.autoCancel();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderAutoCancelTask.task" + " | msg : " + e.getMessage());
        }
    }
}
