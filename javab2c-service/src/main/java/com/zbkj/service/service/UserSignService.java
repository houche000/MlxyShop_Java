package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.user.UserSign;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.UserSignInfoResponse;
import com.zbkj.common.vo.SystemGroupDataSignConfigVo;
import com.zbkj.common.vo.UserSignMonthVo;
import com.zbkj.common.vo.UserSignVo;

import java.util.HashMap;
import java.util.List;

/**
 * UserSignService 接口实现
 */
public interface UserSignService extends IService<UserSign> {

    /**
     * 用户金豆列表
     *
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<UserSignVo> getList(PageParamRequest pageParamRequest);

    List<UserSign> getListByCondition(UserSign sign, PageParamRequest pageParamRequest);

    /**
     * 签到
     * @return SystemGroupDataSignConfigVo
     */
    SystemGroupDataSignConfigVo sign();

    /**
     * 今日记录详情
     * @return HashMap
     */
    HashMap<String, Object> get();

    /**
     * 签到配置
     * @return List<SystemGroupDataSignConfigVo>
     */
    List<SystemGroupDataSignConfigVo> getSignConfig();

    /**
     * 金豆月度列表
     *
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<UserSignMonthVo> getListGroupMonth(PageParamRequest pageParamRequest);

    /**
     * 获取用户签到信息
     * @return UserSignInfoResponse
     */
    UserSignInfoResponse getUserSignInfo();
}