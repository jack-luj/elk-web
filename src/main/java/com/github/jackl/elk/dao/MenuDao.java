package com.github.jackl.elk.dao;


import com.github.jackl.elk.entity.MenuEntity;

import java.util.List;

/**
 * Created by jackl on 2017/2/13.
 */
public interface MenuDao extends BaseDao<MenuEntity>{

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<MenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<MenuEntity> queryNotButtonList();

}
