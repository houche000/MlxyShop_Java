package com.zbkj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.model.finance.UserExtractInfo;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.UserExtractInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: zhongyehai
 * @description:
 * @date: 2022/3/7 17:14
 */

@Slf4j
@RestController
@RequestMapping("api/admin/user/extractInfo")
@Api(tags = "用户 -- 提现账户信息表")
public class UserExtractInfoController {

    @Autowired
    private UserExtractInfoService extractInfoService;

    /**
     * 查询提现绑定账户信息
     */
    @ApiOperation(value = "查询提现绑定账户信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<CommonPage<UserExtractInfo>> list(UserExtractInfo request, @Validated PageParamRequest pageRequest) {
        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.orderByDesc(UserExtractInfo::getId);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<UserExtractInfo> list = extractInfoService.list(queryWrapper);
        return CommonResult.success(CommonPage.restPage(list));
    }
}
