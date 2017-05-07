package com.github.jackl.elk.controller;


import com.github.jackl.elk.entity.MenuEntity;
import com.github.jackl.elk.service.MenuService;
import com.github.jackl.elk.utils.Constant;
import com.github.jackl.elk.utils.JsonResult;
import com.github.jackl.elk.utils.JsonResultException;
import com.github.jackl.elk.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/13.
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController extends AbstractController{
    @Autowired
    private MenuService menuService;

    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public JsonResult list(Integer page, Integer limit){
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<MenuEntity> menuList = menuService.queryList(map);
        int total = menuService.queryTotal(map);

        PageUtils pageUtil = new PageUtils(menuList, total, limit, page);

        return JsonResult.ok().put("page", pageUtil);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public JsonResult select(){
        //查询列表数据
        List<MenuEntity> menuList = menuService.queryNotButtonList();

        //添加顶级菜单
        MenuEntity root = new MenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return JsonResult.ok().put("menuList", menuList);
    }

    /**
     * 角色授权菜单
     */
    @RequestMapping("/perms")
    @RequiresPermissions("sys:menu:perms")
    public JsonResult perms(){
        //查询列表数据
        List<MenuEntity> menuList = menuService.queryList(new HashMap<String, Object>());

        return JsonResult.ok().put("menuList", menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public JsonResult info(@PathVariable("menuId") Long menuId){
        MenuEntity menu = menuService.queryObject(menuId);
        return JsonResult.ok().put("menu", menu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public JsonResult save(@RequestBody MenuEntity menu){
        //数据校验
        verifyForm(menu);

        menuService.save(menu);

        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public JsonResult update(@RequestBody MenuEntity menu){
        //数据校验
        verifyForm(menu);

        menuService.update(menu);

        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    public JsonResult delete(@RequestBody Long[] menuIds){
        for(Long menuId : menuIds){
            if(menuId.longValue() <= 28){
                return JsonResult.error("系统菜单，不能删除");
            }
        }
        menuService.deleteBatch(menuIds);

        return JsonResult.ok();
    }

    /**
     * 用户菜单列表
     */
    @RequestMapping("/user")
    public JsonResult user(){
        List<MenuEntity> menuList = menuService.getUserMenuList(getUserId());

        return JsonResult.ok().put("menuList", menuList);
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(MenuEntity menu){
        if(StringUtils.isBlank(menu.getName())){
            throw new JsonResultException("菜单名称不能为空");
        }

        if(menu.getParentId() == null){
            throw new JsonResultException("上级菜单不能为空");
        }

        //菜单
        if(menu.getType() == Constant.MenuType.MENU.getValue()){
            if(StringUtils.isBlank(menu.getUrl())){
                throw new JsonResultException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            MenuEntity parentMenu = menuService.queryObject(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()){
            if(parentType != Constant.MenuType.CATALOG.getValue()){
                throw new JsonResultException("上级菜单只能为目录类型");
            }
            return ;
        }

        //按钮
        if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
            if(parentType != Constant.MenuType.MENU.getValue()){
                throw new JsonResultException("上级菜单只能为菜单类型");
            }
            return ;
        }
    }

}
