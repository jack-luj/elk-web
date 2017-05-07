package com.github.jackl.elk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jackl on 2017/2/15.
 */

@Controller
public class PageController {

    /**
     * 静态页面处理程序
     * 默认显示login页面
     * @return 返回HTML静态页面
     */
    @RequestMapping("/")
    public String forward() {
        return "forward:/login.html";
    }
}
