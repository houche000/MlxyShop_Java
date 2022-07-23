package com.zbkj.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.zbkj.admin.filter.TokenComponent;
import com.zbkj.admin.service.AdminLoginService;
import com.zbkj.admin.service.ValidateCodeService;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.constants.SysGroupDataConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.system.SystemMenu;
import com.zbkj.common.model.system.SystemPermissions;
import com.zbkj.common.request.SystemAdminLoginRequest;
import com.zbkj.common.response.MenusResponse;
import com.zbkj.common.response.SystemAdminResponse;
import com.zbkj.common.response.SystemGroupDataAdminLoginBannerResponse;
import com.zbkj.common.response.SystemLoginResponse;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.LoginUserVo;
import com.zbkj.common.vo.MenuTree;
import com.zbkj.service.service.SystemAdminService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.SystemGroupDataService;
import com.zbkj.service.service.SystemMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 管理端登录服务实现类
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Resource
    private TokenComponent tokenComponent;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private SystemAdminService systemAdminService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemGroupDataService systemGroupDataService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Autowired
    private CaptchaService captchaService;

    /**
     * PC登录
     */
    @Override
    public SystemLoginResponse login(SystemAdminLoginRequest systemAdminLoginRequest, String ip) {
        // 判断行为验证码
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(systemAdminLoginRequest.getCaptcha().getCaptchaVerification());
        captchaVO.setToken(systemAdminLoginRequest.getCaptcha().getToken());
        captchaVO.setSecretKey(systemAdminLoginRequest.getCaptcha().getSecretKey());
        ResponseModel response = captchaService.verification(captchaVO);
        if(!response.isSuccess() && !response.getRepCode().equals("0000")){
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
            throw new CrmebException("行为验证失败"+response.getRepCode()+"|"+response.getRepMsg());
        }
        // 判断验证码
        boolean codeCheckResult = validateCodeService.check(systemAdminLoginRequest.getKey(), systemAdminLoginRequest.getCode());
        if (!codeCheckResult) throw new CrmebException("验证码不正确");
        // 用户验证
        Authentication authentication = null;
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        try {
//            CusAuthenticationManager authenticationManager = new CusAuthenticationManager(new CustomAuthenticationProvider());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(systemAdminLoginRequest.getAccount(), systemAdminLoginRequest.getPwd()));
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                throw new CrmebException("用户不存在或密码错误");
            }
            throw new CrmebException(e.getMessage());
        }
        LoginUserVo loginUser = (LoginUserVo) authentication.getPrincipal();
        SystemAdmin systemAdmin = loginUser.getUser();

        String token = tokenComponent.createToken(loginUser);
        SystemLoginResponse systemAdminResponse = new SystemLoginResponse();
        systemAdminResponse.setToken(token);
        BeanUtils.copyProperties(systemAdmin, systemAdminResponse);

        //更新最后登录信息
        systemAdmin.setUpdateTime(DateUtil.date());
        systemAdmin.setLoginCount(systemAdmin.getLoginCount() + 1);
        systemAdmin.setLastIp(ip);
        systemAdminService.updateById(systemAdmin);
        return systemAdminResponse;
    }

    /**
     * 用户登出
     */
    @Override
    public Boolean logout() {
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        if (ObjectUtil.isNotNull(loginUserVo)) {
            // 删除用户缓存记录
            tokenComponent.delLoginUser(loginUserVo.getToken());
        }
        return true;
    }

    /**
     * 获取登录页图片
     * @return Map
     */
    @Override
    public Map<String, Object> getLoginPic() {
        Map<String, Object> map = new HashMap<>();
        //背景图
        map.put("backgroundImage", systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ADMIN_LOGIN_BACKGROUND_IMAGE));
        //logo
        map.put("logo", systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ADMIN_LOGIN_LOGO_LEFT_TOP));
        map.put("loginLogo", systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ADMIN_LOGIN_LOGO_LOGIN));
        //轮播图
        List<SystemGroupDataAdminLoginBannerResponse> bannerList = systemGroupDataService.getListByGid(SysGroupDataConstants.GROUP_DATA_ID_ADMIN_LOGIN_BANNER_IMAGE_LIST, SystemGroupDataAdminLoginBannerResponse.class);
        map.put("banner", bannerList);
        return map;
    }

    /**
     * 获取管理员可访问目录
     * @return List<MenusResponse>
     */
    @Override
    public List<MenusResponse> getMenus() {
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        List<String> roleList = Stream.of(loginUserVo.getUser().getRoles().split(",")).collect(Collectors.toList());
        List<SystemMenu> menuList;
        if (roleList.contains("1")) {// 超管
            menuList = systemMenuService.findAllCatalogue();
        } else {
            menuList = systemMenuService.getMenusByUserId(loginUserVo.getUser().getId());
        }
        // 组装前端对象
        List<MenusResponse> responseList = menuList.stream().map(e -> {
            MenusResponse response = new MenusResponse();
            BeanUtils.copyProperties(e, response);
            return response;
        }).collect(Collectors.toList());

        MenuTree menuTree = new MenuTree(responseList);
        return menuTree.buildTree();
    }

    /**
     * 根据Token获取对应用户信息
     */
    @Override
    public SystemAdminResponse getInfoByToken() {
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        SystemAdmin systemAdmin = loginUserVo.getUser();
        SystemAdminResponse systemAdminResponse = new SystemAdminResponse();
        BeanUtils.copyProperties(systemAdmin, systemAdminResponse);
        List<String> roleList = Stream.of(systemAdmin.getRoles().split(",")).collect(Collectors.toList());
        List<String> permList = CollUtil.newArrayList();
        if (roleList.contains("1")) {
            permList.add("*:*:*");
        } else {
            permList = loginUserVo.getPermissions().stream().map(SystemPermissions::getPath).collect(Collectors.toList());
        }
        systemAdminResponse.setPermissionsList(permList);
        return systemAdminResponse;
    }
}
