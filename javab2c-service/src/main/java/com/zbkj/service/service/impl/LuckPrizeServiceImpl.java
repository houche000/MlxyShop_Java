package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.luck.LuckPrize;
import com.zbkj.service.dao.LuckPrizeDao;
import com.zbkj.service.service.LuckPrizeService;
import org.springframework.stereotype.Service;

@Service
public class LuckPrizeServiceImpl extends ServiceImpl<LuckPrizeDao, LuckPrize> implements LuckPrizeService {
}
