package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.wechat.WechatProgramMyTemp;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.WechatProgramMyTempSearchRequest;

import java.util.HashMap;
import java.util.List;

/**
 *  WechatProgramMyTempService 接口
 */
public interface WechatProgramMyTempService extends IService<WechatProgramMyTemp> {

    List<WechatProgramMyTemp> getList(WechatProgramMyTempSearchRequest request, PageParamRequest pageParamRequest);

    void push(int myTempId, HashMap<String, String> map, Integer userId);

}