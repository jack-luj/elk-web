package com.github.jackl.elk.service;


import com.github.jackl.elk.entity.MenuEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
public interface MenuService {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<MenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<MenuEntity> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<MenuEntity> getUserMenuList(Long userId);


    /**
     * 查询菜单
     */
    MenuEntity queryObject(Long menuId);

    /**
     * 查询菜单列表
     */
    List<MenuEntity> queryList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存菜单
     */
    void save(MenuEntity menu);

    /**
     * 修改
     */
    void update(MenuEntity menu);

    /**
     * 删除
     */
    void deleteBatch(Long[] menuIds);
}
