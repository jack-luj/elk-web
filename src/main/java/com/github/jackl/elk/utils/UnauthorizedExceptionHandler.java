package com.github.jackl.elk.utils;

import com.alibaba.fastjson.JSON;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jackl on 2017/2/20.
 */
public class UnauthorizedExceptionHandler implements HandlerExceptionResolver {
    protected Logger _logger = LoggerFactory.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String errorMsg=null;
        if(ex instanceof UnauthorizedException){ //AuthorizationException
            _logger.error("UnauthorizedException"+ ex.toString());
            errorMsg=JSON.toJSONString(new JsonResult().error(403, "权限错误:"+ex.getMessage()));
        }else if(ex instanceof AuthorizationException){
            _logger.error(">>AuthorizationException"+ ex.toString());
            errorMsg=JSON.toJSONString(new JsonResult().error(403, "权限错误:"+ex.getMessage()));
        }else{
            _logger.error("InnerException"+ ex.toString());
            errorMsg=JSON.toJSONString(new JsonResult().error(500, "内部错误:"+ex.getMessage()));
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(errorMsg);
        } catch (IOException e) {
            _logger.error(e.toString());
          //  e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }
}
