package com.github.jackl.elk.controller;

import com.alibaba.fastjson.JSON;

import com.github.jackl.elk.service.GeneratorService;
import com.github.jackl.elk.utils.JsonResult;
import com.github.jackl.elk.utils.PageUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackl on 2017/2/17.
 */
@Controller
@RequestMapping("/api/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("sys:generator:list")
    public JsonResult list(String tableName, Integer page, Integer limit){
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("offset", (page - 1) * limit);
        map.put("limit", limit);

        //查询列表数据
        List<Map<String, Object>> list = generatorService.queryList(map);
        int total = generatorService.queryTotal(map);

        PageUtils pageUtil = new PageUtils(list, total, limit, page);

        return JsonResult.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    @RequiresPermissions("sys:generator:code")
    public void code(String tables, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = generatorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"source.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

}
