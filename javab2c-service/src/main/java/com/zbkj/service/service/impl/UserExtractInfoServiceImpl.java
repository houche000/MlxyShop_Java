package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.finance.UserExtractInfo;
import com.zbkj.service.dao.UserExtractInfoDao;
import com.zbkj.service.service.UserExtractInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/3/7 17:13
 */
@Service
public class UserExtractInfoServiceImpl extends ServiceImpl<UserExtractInfoDao, UserExtractInfo> implements UserExtractInfoService {


    @Autowired
    private UserExtractInfoDao userExtractInfoDao;

    @Override
    public UserExtractInfo getUserExtractInfoByUserId(Integer userId) {
        QueryWrapper<UserExtractInfo> extractInfoQueryWrapper = new QueryWrapper<>();
        extractInfoQueryWrapper.eq("uid",userId);
        UserExtractInfo userExtractInfo = userExtractInfoDao.selectOne(extractInfoQueryWrapper);
        return userExtractInfo;
    }
}
