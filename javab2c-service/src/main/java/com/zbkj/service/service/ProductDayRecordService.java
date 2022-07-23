package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.record.ProductDayRecord;
import com.zbkj.common.request.ProductRankingRequest;

/**
 * ProductDayRecordService 接口
 */
public interface ProductDayRecordService extends IService<ProductDayRecord> {

    /**
     * 获取商品排行榜
     * @param request 查询参数
     * @return PageInfo
     */
    PageInfo<ProductDayRecord> getRanking(ProductRankingRequest request);
}