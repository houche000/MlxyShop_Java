package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.user.UserFeedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:27
 */
@Mapper
public interface UserFeedbackDao extends BaseMapper<UserFeedback> {
}
