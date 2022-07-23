package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.finance.UserExtractInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户提现表 Mapper 接口
 */
@Mapper
@Repository
public interface UserExtractInfoDao extends BaseMapper<UserExtractInfo> {

}
