package com.github.jackl.elk.proxy.fetch;

import com.github.jackl.elk.entity.Proxy;
import com.github.jackl.elk.entity.ProxyEntity;
import com.github.jackl.elk.service.ProxyService;
import com.github.jackl.elk.utils.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * Created by jackl on 2017/6/22.
 */
@Component
public class ProxyPipeline implements Pipeline {
    @Autowired
    ProxyService proxyService;
    private Logger logger= LoggerFactory.getLogger(getClass());
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Proxy> proxies=(List<Proxy>)resultItems.getAll().get("proxies");
        if(proxies!=null){
            logger.info("共获取代理 "+proxies.size()+" 个");
            for (Proxy proxy:proxies) {
                proxyService.save(proxy);
            }

        }
    }
}
