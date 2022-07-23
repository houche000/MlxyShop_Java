package com.zbkj.common.response;

import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserFeedback;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:38
 */
@Data
public class UserFeedbackResponse extends UserFeedback {

    @ApiModelProperty("用户信息")
    private User userInfo;
}
