package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.presale.StorePresaleUser;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StorePresaleUserSearchRequest;

import java.util.List;

/**
 * StorePinkService
 */
public interface StorePresaleUserService extends IService<StorePresaleUser> {

    /**
     * 获取预售用户列表
     * @param request
     * @param pageParamRequest
     * @return
     */
    PageInfo<StorePresaleUser> getList(StorePresaleUserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取预售用户列表
     * @param request
     * @param pageParamRequest
     * @return
     */
    PageInfo<StorePresaleUser> getH5List(StorePresaleUserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取预售用户列表
     * @param preid
     * @return
     */
    List<StorePresaleUser> getListByCid(Integer preid);

    /**
     * 检测是否可以创建订单
     * @param presale
     * @return
     */
    Boolean isCreateOrder(Integer presale);

    /**
     * 获取当前用户预约信息
     * @param presaleId 预售商品id
     * @return
     */
    List<StorePresaleUser> getPresaleByPresaleId(Integer presaleId);

    /**
     * 根据已/未中奖状态获取用户信息
     * @param presaleId 预售商品id
     * @param isWinner 中奖状态
     * @return
     */
    List<StorePresaleUser> getUserByPresaleIdAndIsWinner(Integer presaleId,Integer isWinner);


    /**
     * 实体查询
     * @param storePresaleUser
     * @return
     */
    List<StorePresaleUser> getByEntity(StorePresaleUser storePresaleUser);

    /**
     * 更新预约中签用户的订单号
     * @param presaleId
     * @return
     */
    Boolean updateOrderId(Integer presaleId,String orderId);

    /**
     * PC预售用户详情列表
     *
     * @param pinkId 团长pinkId
     * @return
     */
//    List<StorePinkDetailResponse> getAdminList(Integer pinkId);

    /**
     * PC预售用户详情列表
     *
     * @param request 参数
     * @return
     */
//    PageInfo<StorePinkDetailResponse> getAdminList(StorePresaleUserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 查询预售用户列表
     *
     * @param cid
     * @param kid
     */
//    List<StorePresaleUser> getListByCidAndKid(Integer cid, Integer kid, Integer limit);

    /**
     * 根据团长预售用户id获取预售用户人数
     * @param pinkId
     * @return
     */
//    Integer getCountByKid(Integer pinkId);

    /**
     * 检查状态，更新数据
     */
//    void detectionStatus();

    /**
     * 预售用户成功
     * @param kid
     * @return
     */
//    boolean pinkSuccess(Integer kid);

    /**
     * 根据订单编号获取
     * @param orderId
     * @return
     */
    StorePresaleUser getByOrderId(String orderId);

    /**
     * 获取最后3个预售用户信息（不同用户）
     * @return List
     */
//    List<StorePresaleUser> findSizePink(Integer size);

    /**
     * 获取预售用户参与总人数
     *
     * @return Integer
     */
//    Integer getTotalPeople();

    /**
     * 预售用户订单添加机器人
     */
    Boolean addBotToPink(Integer id);
}