package com.zbkj.front.service;


import com.zbkj.common.request.IosBindingPhoneRequest;
import com.zbkj.common.request.IosLoginRequest;
import com.zbkj.common.request.WxBindingPhoneRequest;
import com.zbkj.common.response.LoginResponse;

/**
 * IOS服务类
 */
public interface IosService {

    /**
     * ios登录
     * @param loginRequest 登录请求对象
     */
    LoginResponse login(IosLoginRequest loginRequest);

    /**
     * IOS绑定手机号
     * @param request 绑定请求对象
     * @return 登录信息
     */
    LoginResponse registerBindingPhone(WxBindingPhoneRequest request);

    /**
     * ios绑定手机号（登录后）
     * @param request 请求对象
     * @return 是否绑定
     */
    Boolean bindingPhone(IosBindingPhoneRequest request);
}
