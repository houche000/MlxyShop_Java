package com.zbkj.admin.task.pink;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.combination.StoreCombination;
import com.zbkj.common.model.combination.StorePink;
import com.zbkj.common.model.user.User;
import com.zbkj.service.service.StoreCombinationService;
import com.zbkj.service.service.StorePinkService;
import com.zbkj.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: zhongyehai
 * @description: 自动拼团类
 * @date: 2022/2/28 14:35
 */
@Component("AutoStartPinkTask")
@Slf4j
public class AutoStartPinkTask {

    @Autowired
    private UserService userService;

    @Autowired
    private StoreCombinationService combinationService;

    @Autowired
    private StorePinkService pinkService;


    /**
     * 机器人开团
     *
     * @param id 拼团商品id
     */
    public void startBotPink(String id) {
        StoreCombination combination = combinationService.getById(id);
        if (ObjectUtil.isNull(combination) || combination.getIsDel()) {
            throw new CrmebException("对应拼团商品不存在");
        }

        // 判断拼团活动时效
        if (!combination.getIsShow()) {
            throw new CrmebException("拼团活动已结束");
        }
        if (System.currentTimeMillis() > combination.getStopTime()) {
            throw new CrmebException("拼团活动已结束");
        }
        User user = userService.randomGetOneBot();

        // 生成拼团表数据
        StorePink storePink = new StorePink();
        storePink.setUid(user.getUid());
        storePink.setIsSystem(true);
        storePink.setAvatar(user.getAvatar());
        storePink.setNickname(user.getNickname());
        storePink.setOrderId("0");
        storePink.setOrderIdKey(0);
        storePink.setTotalNum(1);
        storePink.setTotalPrice(BigDecimal.ZERO);
        storePink.setCid(combination.getId());
        storePink.setPid(combination.getProductId());
        storePink.setPeople(combination.getPeople());
        storePink.setPrice(combination.getPrice());
        Integer effectiveTime = combination.getEffectiveTime();// 有效小时数
        DateTime dateTime = cn.hutool.core.date.DateUtil.date();
        storePink.setAddTime(dateTime.getTime());
        DateTime hourTime = cn.hutool.core.date.DateUtil.offsetHour(dateTime, effectiveTime);
        long stopTime = hourTime.getTime();
        if (stopTime > combination.getStopTime()) {
            stopTime = combination.getStopTime();
        }
        storePink.setStopTime(stopTime);
        storePink.setKId(0);
        storePink.setIsTpl(false);
        storePink.setIsRefund(false);
        storePink.setStatus(1);
        pinkService.save(storePink);

    }
}
