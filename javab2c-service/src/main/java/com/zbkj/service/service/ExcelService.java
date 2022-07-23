package com.zbkj.service.service;

import com.zbkj.common.model.luck.LuckLotteryRecord;
import com.zbkj.common.request.*;

/**
* StoreProductService 接口
*  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
*/
public interface ExcelService{

    /**
     * 导出砍价商品
     * @param request 请求参数
     * @return 导出地址
     */
    String exportBargainProduct(StoreBargainSearchRequest request);

    /**
     * 导出拼团商品
     * @param request 请求参数
     * @return 导出地址
     */
    String exportCombinationProduct(StoreCombinationSearchRequest request);

    /**
     * 商品导出
     * @param request 请求参数
     * @return 导出地址
     */
    String exportProduct(StoreProductSearchRequest request);

    /**
     * 订单导出
     *
     * @param request 查询条件
     * @return 文件名称
     */
    String exportOrder(StoreOrderSearchRequest request);

    /**
     * 抽奖记录导出
     *
     * @param request 搜索条件
     */
    String exportLuckLottery(LuckLotteryRecord request);

    /**
     * 用户充值导出
     *
     * @param request 搜索条件
     */
    String exportUserRecharge(UserRechargeSearchRequest request);

    /**
     * 用户提现导出
     *
     * @param request 搜索条件
     */
    String exportUserExtract(UserExtractSearchRequest request);

    /**
     * 用户列表导出
     *
     * @param request 搜索条件
     */
    String exportUser(UserSearchRequest request);

    /**
     * 拼团列表导出
     *
     * @param request 搜索条件
     */
    String exportPink(StorePinkSearchRequest request);

}
