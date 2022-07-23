package com.zbkj.front.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.model.user.UserFeedback;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.UserFeedbackService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/4/1 15:54
 */
@Slf4j
@RestController("UserFeedbackController")
@RequestMapping("api/front/feedback")
@Api(tags = "用户 -- 意见反馈")
public class UserFeedbackController {


    @Autowired
    private UserService userService;


    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 意见反馈
     *
     * @param request 意见反馈
     * @return 绑定结果
     */
    @ApiOperation(value = "提交意见反馈")
    @RequestMapping(value = "/user/feedback", method = RequestMethod.POST)
    public CommonResult<Boolean> feedback(UserFeedback request) {
        request.setUid(userService.getUserId());
        request.setStatus(0);
        request.setResult(null);
        request.setCreateTime(new Date());
        String imgs = request.getImg().replace("[\"", "").replace("\"]", "")
                .replace("\"", "");
        request.setImg(systemAttachmentService.clearPrefix(ArrayUtils.toString(imgs)));
        return CommonResult.success(userFeedbackService.save(request));
    }


    /**
     * 查询意见反馈列表信息
     */
    @ApiOperation(value = "查询意见反馈列表信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<UserFeedback>> list(UserFeedback request, @Validated PageParamRequest pageRequest) {
        request.setUid(userService.getUserId());
        LambdaQueryWrapper<UserFeedback> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.orderByDesc(UserFeedback::getId);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<UserFeedback> list = userFeedbackService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            CommonResult.success(CommonPage.restPage(Collections.EMPTY_LIST));
        }
        CommonPage<UserFeedback> commonPage = CommonPage.restPage(list);
        return CommonResult.success(commonPage);
    }

}
