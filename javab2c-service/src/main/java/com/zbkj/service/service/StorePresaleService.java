package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.presale.StorePresale;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;

import java.util.List;
import java.util.Map;

/**
 * StorePresaleService
 */
public interface StorePresaleService extends IService<StorePresale> {

    /**
     * 分页显示预售商品表
     * @param request   搜索条件
     * @param pageParamRequest  分页参数
     */
    PageInfo<StorePresaleResponse> getList(StorePresaleSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 新增预售商品
     */
    Integer saveCombination(StorePresaleRequest request);

    /**
     * 删除预售商品
     */
    Boolean deleteById(Integer id);

    /**
     * 编辑预售商品
     */
    Boolean updateCombination(StorePresaleRequest request);

    /**
     * 查询预售商品详情
     * @return StoreProductInfoResponse
     */
    StoreProductInfoResponse getAdminDetail(Integer id);

    /**
     * 修改预售商品状态
     */
    Boolean updateCombinationShow(Integer id, Boolean isShow);


    /**
     * 预售商品抽签
     * @return
     */
    String presaleGoodsWinner();

    /**
     * admin预售统计
     */
//    Map<String, Object> getAdminStatistics();

    /**
     * H5预售商品列表
     */
    List<StorePresaleH5Response> getH5List(ProductRequest request, PageParamRequest pageParamRequest);

    /**
     * H5预售商品详情
     *
     * @param id 预售商品编号
     */
    PresaleDetailResponse getH5Detail(Integer id);



    /**
     * 去预约
     *
     * @param presaleId 预售商品ID
     */
    Boolean goPresale(Integer presaleId);


    /**
     * 更多预售信息
     */
    PageInfo<StorePresale> getMore(PageParamRequest pageParamRequest, Integer comId);



    /**
     * 后台任务批量操作库存
     */
    void consumeProductStock();

    /**
     * 获取当前时间的预售商品
     */
    List<StorePresale> getCurrentBargainByProductId(Integer productId);

    /**
     * 商品是否存在预售活动
     * @param productId 商品编号
     */
    Boolean isExistActivity(Integer productId);

    /**
     * 查询带异常
     * @param combinationId 预售商品id
     * @return StorePresale
     */
    StorePresale getByIdException(Integer combinationId);

    /**
     * 添加/扣减库存
     * @param id 秒杀商品id
     * @param num 数量
     * @param type 类型：add—添加，sub—扣减
     */
    Boolean operationStock(Integer id, Integer num, String type);

    /**
     * 预售首页数据
     * @return CombinationIndexResponse
     */
    PresaleIndexResponse getIndexInfo();

    /**
     * 预售列表header
     *
     * @return CombinationHeaderResponse
     */
//    CombinationHeaderResponse getHeader();

}
