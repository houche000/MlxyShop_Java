package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.video.PayComponentCat;
import com.zbkj.common.vo.CatItem;

import java.util.List;

/**
 *
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface PayComponentCatService extends IService<PayComponentCat> {

    /**
     * 自动更新自定义交易组件类目
     */
    void autoUpdate();

    /**
     * 获取类目
     * @return List<FirstCatVo>
     */
    List<CatItem> getList();

    /**
     * 根据第三级id获取类目
     * @param thirdCatId 第三级id
     * @return PayComponentCat
     */
    PayComponentCat getByThirdCatId(Integer thirdCatId);
}
