package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 用户确认收货Task
 */
@Component("OrderReceiptTask")
public class OrderReceiptTask {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderReceiptTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 1分钟同步一次数据
     */
    public void orderReceipt() {
        //cron : 0 */1 * * * ?
        logger.info("---OrderReceiptTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.orderReceiving();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderReceiptTask.task" + " | msg : " + e.getMessage());
        }
    }

}
