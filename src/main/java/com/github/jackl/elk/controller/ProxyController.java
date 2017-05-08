package com.github.jackl.elk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.jackl.elk.entity.Proxy;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.github.jackl.elk.utils.JsonResult;
import com.github.jackl.elk.entity.ProxyEntity;
import com.github.jackl.elk.service.ProxyService;
import com.github.jackl.elk.utils.PageUtils;


/**
 * 
 * 
 * @author jackl
 * @date 2017-05-08 17:03:11
 */
@RestController
@RequestMapping("api/proxy")
public class ProxyController {
	@Autowired
	private ProxyService proxyService;
	

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("proxy:list")
	public JsonResult list(Integer page, Integer limit,String type){
		type=(type==null)?"all":type;
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("type", "avail");
		
		//查询列表数据
		List proxyList=proxyService.queryList(map);
		int total = proxyService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(proxyList, total, limit, page);
		
		return JsonResult.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("proxy:info")
	public JsonResult info(@PathVariable("id") Long id){
		ProxyEntity proxy = proxyService.queryObject(id);
		
		return JsonResult.ok().put("proxy", proxy);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("proxy:save")
	public JsonResult save(@RequestBody ProxyEntity proxy){
        verifyForm(proxy);
		proxyService.save(proxy);
		
		return JsonResult.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("proxy:update")
	public JsonResult update(@RequestBody ProxyEntity proxy){
        verifyForm(proxy);
		proxyService.update(proxy);
		
		return JsonResult.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("proxy:delete")
	public JsonResult delete(@RequestBody Long[] ids){
		proxyService.deleteBatch(ids);
		
		return JsonResult.ok();
	}

    /**
 * 验证参数是否正确
 */
    private void verifyForm(ProxyEntity proxy){


    }

}
