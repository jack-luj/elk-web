package com.github.jackl.elk.dao;


import com.github.jackl.elk.entity.RoleMenuEntity;

import java.util.List;

/**
 * Created by jackl on 2017/2/13.
 */
public interface RoleMenuDao extends BaseDao<RoleMenuEntity>{
    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);
}
