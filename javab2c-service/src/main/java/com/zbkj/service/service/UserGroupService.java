package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.user.UserGroup;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserGroupRequest;

import java.util.List;

/**
 * UserGroupService 接口实现
 */
public interface UserGroupService extends IService<UserGroup> {

    /**
     * 会员-分组列表
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<UserGroup> getList(PageParamRequest pageParamRequest);

    String getGroupNameInId(String groupIdValue);

    /**
     * 新增用户分组
     * @param userGroupRequest 分组参数
     */
    Boolean create(UserGroupRequest userGroupRequest);

    /**
     * 修改用户分组
     * @param id 分组id
     * @param userGroupRequest 修改参数
     */
    Boolean edit(Integer id, UserGroupRequest userGroupRequest);
}