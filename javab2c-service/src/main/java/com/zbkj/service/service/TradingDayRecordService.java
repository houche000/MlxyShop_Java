package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.record.TradingDayRecord;

import java.util.List;

/**
 * TradingDayRecordService 接口
 */
public interface TradingDayRecordService extends IService<TradingDayRecord> {

    /**
     * 根据日期获取记录
     * @param date 日期，yyyy-MM-dd
     * @return TradingDayRecord
     */
    TradingDayRecord getByDate(String date);

    /**
     * 获取时间段内的数据
     * @param startDate 日期，yyyy-MM-dd
     * @param endDate 日期，yyyy-MM-dd
     * @return TradingDayRecord
     */
    TradingDayRecord getByTimeInterval(String startDate, String endDate);

    /**
     * 根据时间范围返回趋势数据
     * @param startDate 开始日期,格式yyyy-MM-dd
     * @param endDate 结束日期,格式yyyy-MM-dd
     * @return List<TradingDayRecord>
     */
    List<TradingDayRecord> findTrendDataOfBetween(String startDate, String endDate);
}