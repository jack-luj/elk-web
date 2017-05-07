package com.github.jackl.elk.service.impl;


import com.github.jackl.elk.dao.MenuDao;
import com.github.jackl.elk.entity.MenuEntity;
import com.github.jackl.elk.service.MenuService;
import com.github.jackl.elk.service.RoleMenuService;
import com.github.jackl.elk.service.UserService;
import com.github.jackl.elk.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<MenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<MenuEntity> menuList = menuDao.queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<MenuEntity> userMenuList = new ArrayList<>();
        for(MenuEntity menu : menuList){
            if(menuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<MenuEntity> queryNotButtonList() {
        return menuDao.queryNotButtonList();
    }

    @Override
    public List<MenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == 1){
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = userService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Override
    public MenuEntity queryObject(Long menuId) {
        return menuDao.queryObject(menuId);
    }

    @Override
    public List<MenuEntity> queryList(Map<String, Object> map) {
        return menuDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return menuDao.queryTotal(map);
    }

    @Override
    public void save(MenuEntity menu) {
        menuDao.save(menu);
    }

    @Override
    public void update(MenuEntity menu) {
        menuDao.update(menu);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] menuIds) {
        menuDao.deleteBatch(menuIds);
    }

    /**
     * 获取所有菜单列表
     */
    private List<MenuEntity> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<MenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<MenuEntity> getMenuTreeList(List<MenuEntity> menuList, List<Long> menuIdList){
        List<MenuEntity> subMenuList = new ArrayList<MenuEntity>();

        for(MenuEntity entity : menuList){
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){//目录
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }
}
