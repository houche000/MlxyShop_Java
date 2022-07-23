package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单自动完成Task
 */
@Component("OrderAutoCompleteTask")
public class OrderAutoCompleteTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderCompleteTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 每小时同步一次数据
     */
    public void autoComplete() {
        // cron : 0 0 */1 * * ?
        logger.info("---OrderAutoCompleteTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.autoComplete();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderAutoCompleteTask.task" + " | msg : " + e.getMessage());
        }
    }

}
