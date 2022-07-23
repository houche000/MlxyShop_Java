package com.zbkj.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserFeedback;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.UserFeedbackResponse;
import com.zbkj.service.service.UserFeedbackService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:35
 */
@Slf4j
@RestController
@RequestMapping("api/admin/user/feedback")
@Api(tags = "用户 -- 意见反馈")
public class UserFeedbackController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeedbackService userFeedbackService;

    /**
     * 查询意见反馈列表信息
     */
    @ApiOperation(value = "查询意见反馈列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<UserFeedbackResponse>> list(UserFeedback request, @Validated PageParamRequest pageRequest) {
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.orderByDesc(UserFeedback::getId);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<UserFeedback> list = userFeedbackService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            CommonResult.success(CommonPage.restPage(Collections.EMPTY_LIST));
        }
        List<UserFeedbackResponse> responseList = new ArrayList<>(list.size());
        List<Integer> uids = list.stream().map(UserFeedback::getUid).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(uids)) {
            Map<Integer, User> userMap = userService.listByIds(uids).stream().collect(Collectors.toMap(User::getUid, Function.identity(), (e1, e2) -> e1));
            for (UserFeedback feedback : list) {
                UserFeedbackResponse response = new UserFeedbackResponse();
                BeanUtils.copyProperties(feedback, response);
                response.setUserInfo(userMap.get(feedback.getUid()));
                responseList.add(response);
            }
        }
        CommonPage<UserFeedback> commonPage = CommonPage.restPage(list);
        CommonPage<UserFeedbackResponse> restPage = CommonPage.restPage(responseList);
        BeanUtils.copyProperties(commonPage, restPage, "list");
        return CommonResult.success(restPage);
    }

    @ApiModelProperty("处理意见反馈")
    @PostMapping("/update")
    public CommonResult<Boolean> update(UserFeedback request) {
        request.setContent(null);
        request.setImg(null);
        request.setUid(null);
        request.setResultTime(new Date());
        return CommonResult.success(userFeedbackService.updateById(request));
    }
}
