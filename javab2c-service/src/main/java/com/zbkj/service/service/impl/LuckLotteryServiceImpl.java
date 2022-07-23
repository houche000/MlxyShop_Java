package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.luck.LuckLottery;
import com.zbkj.service.dao.LuckLotteryDao;
import com.zbkj.service.service.LuckLotteryService;
import org.springframework.stereotype.Service;

@Service
public class LuckLotteryServiceImpl extends ServiceImpl<LuckLotteryDao, LuckLottery> implements LuckLotteryService {
}
