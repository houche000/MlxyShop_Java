package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.system.SystemStore;
import com.zbkj.common.request.StoreNearRequest;
import com.zbkj.common.vo.SystemStoreNearVo;

import java.util.List;

/**
 * 门店自提 Mapper 接口
 */
public interface SystemStoreDao extends BaseMapper<SystemStore> {

    List<SystemStoreNearVo> getNearList(StoreNearRequest request);
}

