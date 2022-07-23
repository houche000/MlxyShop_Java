package com.zbkj.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zbkj.common.model.finance.UserExtractInfo;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.UserExtractInfoService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("api/front/extractInfo")
@Api(tags = "用户 -- 提现账户信息表")
public class UserExtractInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserExtractInfoService extractInfoService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 查询提现绑定账户信息
     */
    @ApiOperation(value = "查询提现绑定账户信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResult<List<UserExtractInfo>> list(UserExtractInfo request) {
        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>(request);
        queryWrapper.eq(UserExtractInfo::getUid, userService.getUserId());
        queryWrapper.orderByDesc(UserExtractInfo::getId);
        List<UserExtractInfo> list = extractInfoService.list(queryWrapper);
        return CommonResult.success(list);
    }

    /**
     * 查询指定类型提现绑定账户信息
     */
    @ApiOperation(value = "查询指定类型提现绑定账户信息")
    @RequestMapping(value = "/get/{type}", method = RequestMethod.POST)
    public CommonResult<UserExtractInfo> list(@PathVariable("type") String type) {
        if (StringUtils.isBlank(type)) {
            return CommonResult.failed("类型必须传递");
        }
        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtractInfo::getUid, userService.getUserId());
        queryWrapper.eq(UserExtractInfo::getExtractType, type);
        UserExtractInfo extractInfo = extractInfoService.getOne(queryWrapper);
        return CommonResult.success(extractInfo);
    }


    /**
     * 保存提现绑定账户信息
     */
    @ApiOperation(value = "保存提现绑定账户信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<Boolean> save(UserExtractInfo request) {
        if (StringUtils.isBlank(request.getExtractType())) {
            return CommonResult.failed("类型必须传递");
        }

        LambdaQueryWrapper<UserExtractInfo> queryWrapperTwo = new LambdaQueryWrapper<>();
        queryWrapperTwo.eq(UserExtractInfo::getAccountName, request.getAccountName()).or().eq(UserExtractInfo::getIcNumber, request.getIcNumber());
        int countTwo = extractInfoService.count(queryWrapperTwo);
        if (countTwo > 0) {
            return CommonResult.failed("该ICNumber/银行卡已存在,请编辑");
        }

        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtractInfo::getUid, userService.getUserId());
        queryWrapper.eq(UserExtractInfo::getExtractType, request.getExtractType());
        int count = extractInfoService.count(queryWrapper);
        if (count > 0) {
            return CommonResult.failed("该类型已存在，请编辑");
        }
        request.setBankImage(systemAttachmentService.clearPrefix(request.getBankImage()));
        request.setAddress(systemAttachmentService.clearPrefix(request.getAddress()));
        request.setUid(userService.getUserId());
        extractInfoService.save(request);
        return CommonResult.success(true);
    }

    /**
     * 编辑提现绑定账户信息
     */
    @ApiOperation(value = "编辑提现绑定账户信息")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonResult<Boolean> edit(UserExtractInfo request) {
        if (StringUtils.isBlank(request.getExtractType())) {
            return CommonResult.failed("类型必须传递");
        }
        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtractInfo::getUid, userService.getUserId());
        queryWrapper.eq(UserExtractInfo::getExtractType, request.getExtractType());
        UserExtractInfo info = extractInfoService.getOne(queryWrapper);
        if (info == null) {
            return CommonResult.failed("该类型不存在，请新增");
        }
        request.setId(info.getId());
        request.setBankImage(systemAttachmentService.clearPrefix(request.getBankImage()));
        request.setAddress(systemAttachmentService.clearPrefix(request.getAddress()));
        request.setIcNumber(info.getIcNumber());
        request.setUid(userService.getUserId());
        extractInfoService.updateById(request);
        return CommonResult.success(true);
    }

    /**
     * 删除提现绑定账户信息
     */
    @ApiOperation(value = "删除提现绑定账户信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Boolean> delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return CommonResult.failed("id必须传递");
        }
        LambdaQueryWrapper<UserExtractInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserExtractInfo::getId, id);
        queryWrapper.eq(UserExtractInfo::getUid, userService.getUserId());
        int count = extractInfoService.count(queryWrapper);
        if (count == 0) {
            return CommonResult.failed("该类型不存在");
        }
        extractInfoService.removeById(id);
        return CommonResult.success(true);
    }


}
