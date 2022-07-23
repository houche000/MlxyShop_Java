package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.wechat.video.PayComponentCat;
import org.apache.ibatis.annotations.Update;

/**
 * 组件类目表 Mapper 接口
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentCatDao extends BaseMapper<PayComponentCat> {

    @Update("truncate table eb_pay_component_cat")
    void deleteAll();

}
