package com.zbkj.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.TopDetail;
import com.zbkj.common.response.UserResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

/**
 * 用户表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/user")
@Api(tags = "会员管理")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 分页显示用户表
     *
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:user:list')")
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserResponse>> getList(@ModelAttribute @Validated UserSearchRequest request,
                                                          @ModelAttribute PageParamRequest pageParamRequest) {
        CommonPage<UserResponse> userCommonPage = CommonPage.restPage(userService.getList(request, pageParamRequest));
        return CommonResult.success(userCommonPage);
    }

    /**
     * 分页显示机器人用户表
     *
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:user:list')")
    @ApiOperation(value = "分页显示机器人用户")
    @RequestMapping(value = "/listBot", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserResponse>> listBot(@ModelAttribute @Validated UserSearchRequest request,
                                                          @ModelAttribute PageParamRequest pageParamRequest) {
        CommonPage<UserResponse> userCommonPage = CommonPage.restPage(userService.listBot(request, pageParamRequest));
        return CommonResult.success(userCommonPage);
    }

    /**
     * 修改用户表
     *
     * @param id          integer id
     * @param userRequest 修改参数
     */
    @PreAuthorize("hasAuthority('admin:user:update')")
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestParam Integer id, @RequestBody @Validated UserUpdateRequest userRequest) {
        userRequest.setUid(id);
        if (userService.updateUser(userRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "后台修改密码")
    @PreAuthorize("hasAuthority('admin:user:update')")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public CommonResult<Boolean> updatePassword(@RequestBody User user) {
        if (user.getUid() == null) {
            return CommonResult.failed("用户id不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return CommonResult.failed("密码不能为空");
        }
        User tempUser = userService.getInfoByUid(user.getUid());
        if (tempUser == null) {
            return CommonResult.failed("用户不存在");
        }
        user.setPwd(CrmebUtil.encryptPassword(user.getPassword(), tempUser.getPhone()));
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPwd, user.getPwd());
        updateWrapper.eq(User::getUid, user.getUid());
        return CommonResult.success(userService.update(updateWrapper));
    }

    /**
     * 修改机器人表
     *
     * @param id          integer id
     * @param userRequest 修改参数
     */
    @PreAuthorize("hasAuthority('admin:user:update')")
    @ApiOperation(value = "修改机器人信息")
    @RequestMapping(value = "/updateBotUser", method = RequestMethod.POST)
    public CommonResult<String> updateBotUser(@RequestParam Integer id,@RequestBody UserUpdateRequest userRequest) {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUid(id);
        if (StringUtils.isNotBlank(userRequest.getNickname())) {
            request.setNickname(userRequest.getNickname());
        }
        if (StringUtils.isNotBlank(userRequest.getRealName())) {
            request.setRealName(userRequest.getRealName());
        }
        request.setAvatar(systemAttachmentService.clearPrefix(userRequest.getAvatar()));
        request.setSex(userRequest.getSex());
        if (userService.updateBotUser(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 新增用户
     *
     * @param user 参数
     */
    @PreAuthorize("hasAuthority('admin:user:update')")
    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonResult<String> addUser(@RequestBody User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return CommonResult.failed("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return CommonResult.failed("密码不能为空");
        }
        LambdaQueryWrapper<User> query = Wrappers.lambdaQuery();
        query.eq(User::getAccount, user.getAccount());
        int count = userService.count(query);
        if (count > 0) {
            return CommonResult.failed("用户名已存在");
        }
        user.setUid(null);
        user.setUserType(Constants.USER_LOGIN_TYPE_H5);
        user.setLoginType(Constants.USER_LOGIN_TYPE_H5);
        if (user.getAvatar() == null) {
            user.setAvatar(systemConfigService.getValueByKey(Constants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        } else {
            user.setAvatar(systemAttachmentService.clearPrefix(user.getAvatar()));
        }
        user.setPwd(CrmebUtil.encryptPassword(user.getPassword(), user.getPhone()));
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setUpdateTime(nowDate);
        user.setLastLoginTime(nowDate);
        if (userService.save(user)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 添加机器人表
     *
     * @param user 参数
     */
    @PreAuthorize("hasAuthority('admin:user:update')")
    @ApiOperation(value = "添加机器人")
    @RequestMapping(value = "/addBotUser", method = RequestMethod.POST)
    public CommonResult<String> addBotUser(@RequestBody User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return CommonResult.failed("用户名不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return CommonResult.failed("密码不能为空");
        }
        LambdaQueryWrapper<User> query = Wrappers.lambdaQuery();
        query.eq(User::getAccount, user.getAccount());
        int count = userService.count(query);
        if (count > 0) {
            return CommonResult.failed("用户名已存在");
        }
        user.setUid(null);
        user.setUserType(Constants.USER_LOGIN_TYPE_BOT);
        user.setLoginType(Constants.USER_LOGIN_TYPE_BOT);
        user.setPwd(CrmebUtil.encryptPassword(user.getPassword(), user.getPhone()));
        if (user.getAvatar() == null) {
            user.setAvatar(systemConfigService.getValueByKey(Constants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        } else {
            user.setAvatar(systemAttachmentService.clearPrefix(user.getAvatar()));
        }
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setUpdateTime(nowDate);
        user.setLastLoginTime(nowDate);
        if (userService.save(user)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 修改用户手机号
     *
     * @param id    用户uid
     * @param phone 手机号
     */
    @PreAuthorize("hasAuthority('admin:user:update:phone')")
    @ApiOperation(value = "修改用户手机号")
    @RequestMapping(value = "/update/phone", method = RequestMethod.GET)
    public CommonResult<String> updatePhone(@RequestParam(name = "id") Integer id, @RequestParam(name = "phone") String phone) {
        if (userService.updateUserPhone(id, phone)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 用户详情
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:user:info')")
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<User> info(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(userService.getInfoByUid(id));
    }

    /**
     * 根据参数类型查询会员对应的信息
     *
     * @param userId           Integer 会员id
     * @param type             int 类型 0=消费记录，1=金豆明细，2=签到记录，3=持有优惠券，4=余额变动，5=好友关系
     * @param pageParamRequest PageParamRequest 分页
     */
    @PreAuthorize("hasAuthority('admin:user:infobycondition')")
    @ApiOperation(value="会员详情")
    @RequestMapping(value = "/infobycondition", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",example = "1", required = true),
            @ApiImplicitParam(name = "type", value = "0=消费记录，1=金豆明细，2=签到记录，3=持有优惠券，4=余额变动，5=好友关系", example = "0"
                    , required = true)
    })
    public CommonResult<CommonPage<T>> infoByCondition(@RequestParam(name = "userId") @Valid Integer userId,
                                                       @RequestParam(name = "type") @Valid @Max(5) @Min(0) int type,
                                                       @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage((List<T>) userService.getInfoByCondition(userId,type,pageParamRequest)));
    }

    /**
     * 会员详情页Top数据
     */
    @PreAuthorize("hasAuthority('admin:user:topdetail')")
    @ApiOperation(value = "会员详情页Top数据")
    @RequestMapping(value = "topdetail", method = RequestMethod.GET)
    public CommonResult<TopDetail> topDetail (@RequestParam @Valid Integer userId) {
        return CommonResult.success(userService.getTopDetail(userId));
    }

    /**
     * 操作金豆
     */
    @PreAuthorize("hasAuthority('admin:user:operate:founds')")
    @ApiOperation(value = "金豆余额")
    @RequestMapping(value = "/operate/founds", method = RequestMethod.GET)
    public CommonResult<Object> founds(@Validated UserOperateIntegralMoneyRequest request) {
        if (userService.updateIntegralMoney(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 会员分组
     * @param id String id
     * @param groupId Integer 分组Id
     */
    @PreAuthorize("hasAuthority('admin:user:group')")
    @ApiOperation(value = "分组")
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public CommonResult<String> group(@RequestParam String id, @RequestParam String groupId) {
        if (userService.group(id, groupId)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 会员标签
     * @param id String id
     * @param tagId Integer 标签id
     */
    @PreAuthorize("hasAuthority('admin:user:tag')")
    @ApiOperation(value = "标签")
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public CommonResult<String> tag(@RequestParam String id, @RequestParam String tagId) {
        if (userService.tag(id, tagId)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 修改上级推广人
     */
    @PreAuthorize("hasAuthority('admin:user:update:spread')")
    @ApiOperation(value = "修改上级推广人")
    @RequestMapping(value = "/update/spread", method = RequestMethod.POST)
    public CommonResult<String> editSpread(@Validated @RequestBody UserUpdateSpreadRequest request) {
        if (userService.editSpread(request)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    /**
     * 更新用户会员等级
     */
    @PreAuthorize("hasAuthority('admin:user:update:level')")
    @ApiOperation(value = "更新用户会员等级")
    @RequestMapping(value = "/update/level", method = RequestMethod.POST)
    public CommonResult<Object> updateUserLevel(@Validated @RequestBody UpdateUserLevelRequest request) {
        if (userService.updateUserLevel(request)) {
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }
}



