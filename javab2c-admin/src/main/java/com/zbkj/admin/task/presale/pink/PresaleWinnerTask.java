package com.zbkj.admin.task.presale.pink;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.StorePinkService;
import com.zbkj.service.service.StorePresaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 拼团状态变化Task
 */
@Component("PresaleWinnerTask")
public class PresaleWinnerTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(PresaleWinnerTask.class);

    @Autowired
    private StorePresaleService storePresaleService;

    /**
     * 三十秒执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void pinkStatusChage() {
        //每分钟执行一次
        // cron : 0 */1 * * * ?
        logger.info("---PresaleWinnerTask------bargain stop status change task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            storePresaleService.presaleGoodsWinner();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("PinkStatusChange" + " | msg : " + e.getMessage());
        }
    }
}
