package com.zbkj.admin.task.coupon;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.StoreCouponUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优惠券过期定时任务
 */
@Component("CouponOverdueTask")
public class CouponOverdueTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(CouponOverdueTask.class);

    @Autowired
    private StoreCouponUserService couponUserService;

    /**
     * 1分钟同步一次数据
     */
    public void couponOverdue() {
        // cron : 0 */1 * * * ?
        logger.info("---CouponOverdueTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            couponUserService.overdueTask();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("CouponOverdueTask.task" + " | msg : " + e.getMessage());
        }
    }

}
