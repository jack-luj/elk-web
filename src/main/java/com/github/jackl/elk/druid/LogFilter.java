package com.github.jackl.elk.druid;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jackl on 2017/2/13.
 */
@WebFilter(filterName="logFilter",urlPatterns="/*")
public class LogFilter implements Filter {
    FilterConfig config;
    private Logger logger = LoggerFactory.getLogger(LogFilter.class);
    @Value("${logFilter.disabled}")
    private  boolean disabled;

    public void destroy() {
        this.config = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest hreq = (HttpServletRequest) req;
        // log
        if(!disabled) {
            logger.info(handleIp(hreq.getRemoteAddr()) + "   " + hreq.getMethod() + "   " + hreq.getServletPath() + "   " + JSON.toJSONString(req.getParameterMap()));
        }
        try {
            chain.doFilter(req, res);
        } catch (IOException ioe) {
            //ioe.printStackTrace();
            logger.error(ioe.toString());
        }
    }

    public void init(FilterConfig config) throws ServletException {

        this.config = config;
    }

    public  String handleIp(String originalIpStr){
        String re="";
        try{
            if(originalIpStr!=null){
                InetAddress inetAddress=InetAddress.getByName(originalIpStr);
                String originalIp=inetAddress.getHostAddress();

                if(inetAddress instanceof Inet6Address){//v6
                    String ip=originalIp;
                    originalIp = originalIp.replaceFirst("0:0:0:0:0:0:0:", "::");
                    if(originalIp.length()==ip.length()) {
                        originalIp = originalIp.replaceFirst(":0:0:0:0:0:0:", "::");
                    }
                    if(originalIp.length()==ip.length()) {
                        originalIp = originalIp.replaceFirst(":0:0:0:0:0:", "::");
                    }
                    if(originalIp.length()==ip.length()) {
                        originalIp = originalIp.replaceFirst(":0:0:0:0:", "::");
                    }
                    if(originalIp.length()==ip.length()) {
                        originalIp = originalIp.replaceFirst(":0:0:0:", "::");
                    }
                    if(originalIp.length()==ip.length()){
                        originalIp=originalIp.replaceFirst(":0:0:","::");
                    }
                    if(originalIp.length()==ip.length()){
                        originalIp=originalIp.replaceFirst(":0:","::");
                    }
                }
                re=originalIp;
            }
        }catch (UnknownHostException e){
           // e.printStackTrace();
            logger.error(e.toString());
        }
        return re;
    }

}