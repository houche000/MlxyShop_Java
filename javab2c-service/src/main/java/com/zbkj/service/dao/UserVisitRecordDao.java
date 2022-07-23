package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.record.UserVisitRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 用户访问记录表 Mapper 接口
 */
public interface UserVisitRecordDao extends BaseMapper<UserVisitRecord> {

    /**
     * 获取活跃用户数
     * @param date 日期
     * @return Integer
     */
    Integer getActiveUserNumByDate(@Param("date") String date);

    /**
     * 通过时间段获取活跃用户数
     * @param startDate 日期
     * @param endDate 日期
     * @return Integer
     */
    Integer getActiveUserNumByPeriod(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
