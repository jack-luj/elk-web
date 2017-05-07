package com.github.jackl.elk.service;

import java.util.List;

/**
 * Created by jackl on 2017/2/13.
 */
public interface RoleMenuService {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);
}
