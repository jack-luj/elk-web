package com.github.jackl.elk.controller;


import com.github.jackl.elk.entity.RoleEntity;
import com.github.jackl.elk.service.RoleMenuService;
import com.github.jackl.elk.service.RoleService;
import com.github.jackl.elk.utils.JsonResult;
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
 * Created by jackl on 2017/2/15.
 */
@RestController
@RequestMapping("/api/role")
public class RoleController extends AbstractController{

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public JsonResult list(Integer page, Integer limit){
        Map<String, Object> map = new HashMap<>();
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<RoleEntity> list = roleService.queryList(map);
        int total = roleService.queryTotal(map);

        PageUtils pageUtil = new PageUtils(list, total, limit, page);

        return JsonResult.ok().put("page", pageUtil);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public JsonResult select(){
        //查询列表数据
        List<RoleEntity> list = roleService.queryList(new HashMap<String, Object>());

        return JsonResult.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public JsonResult info(@PathVariable("roleId") Long roleId){
        RoleEntity role = roleService.queryObject(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = roleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        return JsonResult.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public JsonResult save(@RequestBody RoleEntity role){
        if(StringUtils.isBlank(role.getRoleName())){
            return JsonResult.error("角色名称不能为空");
        }

        roleService.save(role);

        return JsonResult.ok();
    }

    /**
     * 修改角色
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public JsonResult update(@RequestBody RoleEntity role){
        if(StringUtils.isBlank(role.getRoleName())){
            return JsonResult.error("角色名称不能为空");
        }

        roleService.update(role);

        return JsonResult.ok();
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public JsonResult delete(@RequestBody Long[] roleIds){
        roleService.deleteBatch(roleIds);

        return JsonResult.ok();
    }
}
