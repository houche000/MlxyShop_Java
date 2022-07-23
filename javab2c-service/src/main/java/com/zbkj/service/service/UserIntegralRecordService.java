package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.user.UserIntegralRecord;
import com.zbkj.common.request.AdminIntegralSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.UserIntegralRecordResponse;

import java.util.List;

/**
 * 用户金豆记录Service
 */
public interface UserIntegralRecordService extends IService<UserIntegralRecord> {

    /**
     * 根据订单编号、uid获取记录列表
     * @param orderNo 订单编号
     * @param uid 用户uid
     * @return 记录列表
     */
    List<UserIntegralRecord> findListByOrderIdAndUid(String orderNo, Integer uid);

    /**
     * 金豆解冻
     */
    void integralThaw();

    /**
     * PC后台列表
     * @param request 搜索条件
     * @param pageParamRequest 分页参数
     * @return 记录列表
     */
    PageInfo<UserIntegralRecordResponse> findAdminList(AdminIntegralSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 根据类型条件计算金豆总数
     *
     * @param uid      用户uid
     * @param type     类型：1-增加，2-扣减
     * @param date     日期
     * @param linkType 关联类型
     * @return 金豆总数
     */
    Integer getSumIntegral(Integer uid, Integer type, String date, String linkType);

    /**
     * H5用户金豆列表
     *
     * @param uid              用户uid
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<UserIntegralRecord> findUserIntegralRecordList(Integer uid, PageParamRequest pageParamRequest);

    /**
     * 获取用户冻结的金豆
     *
     * @param uid 用户uid
     * @return 金豆数量
     */
    Integer getFrozenIntegralByUid(Integer uid);
}