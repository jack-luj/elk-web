package com.github.jackl.elk.service.impl;


import com.github.jackl.elk.dao.RoleDao;
import com.github.jackl.elk.entity.RoleEntity;
import com.github.jackl.elk.service.RoleMenuService;
import com.github.jackl.elk.service.RoleService;
import com.github.jackl.elk.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao sysRoleDao;
    @Autowired
    private RoleMenuService sysRoleMenuService;
    @Autowired
    private UserRoleService sysUserRoleService;

    @Override
    public RoleEntity queryObject(Long roleId) {
        return sysRoleDao.queryObject(roleId);
    }

    @Override
    public List<RoleEntity> queryList(Map<String, Object> map) {
        return sysRoleDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysRoleDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(RoleEntity role) {
        role.setCreateTime(new Date());
        sysRoleDao.save(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional
    public void update(RoleEntity role) {
        sysRoleDao.update(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] roleIds) {
        sysRoleDao.deleteBatch(roleIds);
    }

}
