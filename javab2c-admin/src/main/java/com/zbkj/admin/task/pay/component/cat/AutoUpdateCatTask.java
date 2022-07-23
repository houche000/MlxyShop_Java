package com.zbkj.admin.task.pay.component.cat;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.PayComponentCatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自动更新自定义交易组件类目定时任务
 */
@Component("AutoUpdateCatTask")
public class AutoUpdateCatTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(AutoUpdateCatTask.class);

    @Autowired
    private PayComponentCatService catService;

    /**
     * 每天凌晨一点执行
     */
    public void autoUpdateCat() {
        // cron : 0 0 1 * * ?
        logger.info("---AutoUpdateCatTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            catService.autoUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderAutoCancelTask.task" + " | msg : " + e.getMessage());
        }
    }

}
