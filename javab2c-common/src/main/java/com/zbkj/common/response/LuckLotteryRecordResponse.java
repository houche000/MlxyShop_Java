package com.zbkj.common.response;

import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.common.model.user.User;
import lombok.Data;

@Data
public class LuckLotteryRecordResponse {

    /**
     * 抽奖活动
     */
    private LuckLottery luckLottery;

    /**
     * 奖品
     */
    private LuckPrize luckPrize;

    /**
     * 抽奖记录
     */
    private LuckLotteryRecord record;

    private User user;

}
