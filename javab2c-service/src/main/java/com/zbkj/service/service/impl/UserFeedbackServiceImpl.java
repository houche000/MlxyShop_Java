package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.user.UserFeedback;
import com.zbkj.service.dao.UserFeedbackDao;
import com.zbkj.service.service.UserFeedbackService;
import org.springframework.stereotype.Service;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:29
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackDao, UserFeedback> implements UserFeedbackService {
}
