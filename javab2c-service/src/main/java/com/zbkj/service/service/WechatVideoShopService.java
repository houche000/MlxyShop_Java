package com.zbkj.service.service;


import com.zbkj.common.vo.BaseResultResponseVo;
import com.zbkj.common.vo.RegisterCheckResponseVo;

/**
 * 视频号交易组件服务——商家入驻部分
 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------

 *  +----------------------------------------------------------------------
 */
public interface WechatVideoShopService {

    /**
     * 接入申请
     * @return 接入结果
     */
    BaseResultResponseVo shopRegisterApply();

    /**
     * 获取接入状态
     * @return 接入状态结果
     */
    RegisterCheckResponseVo shopRegisterCheck();
}
