package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.system.SystemRoleMenu;
import com.zbkj.service.dao.SystemRoleMenuDao;
import com.zbkj.service.service.SystemRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SystemRoleMenuServiceImpl 接口实现
 * 角色菜单关联服务实现
 */
@Service
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuDao, SystemRoleMenu> implements SystemRoleMenuService {

    @Resource
    private SystemRoleMenuDao dao;

    /**
     * 通过角色id删除
     * @param rid 角色id
     */
    @Override
    public Boolean deleteByRid(Integer rid) {
        LambdaUpdateWrapper<SystemRoleMenu> luw = Wrappers.lambdaUpdate();
        luw.eq(SystemRoleMenu::getRid, rid);
        return dao.delete(luw) > 0;
    }

    /**
     * 通过角色id获取菜单列表
     * @param rid 角色id
     * @return List
     */
    @Override
    public List<Integer> getMenuListByRid(Integer rid) {
        LambdaQueryWrapper<SystemRoleMenu> lqw = Wrappers.lambdaQuery();
        lqw.select(SystemRoleMenu::getMenuId);
        lqw.eq(SystemRoleMenu::getRid, rid);
        List<SystemRoleMenu> roleMenuList = dao.selectList(lqw);
        return roleMenuList.stream().map(SystemRoleMenu::getMenuId).collect(Collectors.toList());
    }
}

