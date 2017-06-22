package com.github.jackl.elk.proxy.fetch;

import com.github.jackl.elk.entity.Proxy;
import com.github.jackl.elk.proxy.ProxySite;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackl on 2017/6/22.
 */
@Component
public class ProxyProcessor implements PageProcessor {
    @Autowired
    ProxyPipeline proxyPipeline;
    static final boolean anonymousFlag = true;
    private Logger logger= LoggerFactory.getLogger(getClass());

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .setCharset("UTF-8");

    private static final int voteNum = 1000;


    @Override
    public void process(Page page) {

        logger.info("正在分析>>>>"+page.getUrl().toString());
        if(!page.getUrl().toString().contains("#")){
            List<Proxy> proxies=parse(page.getRawText());
            if(proxies!=null){
                page.putField("proxies",proxies);
            }
        }
    }

    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[id=ip_list] tr[class]");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            String isAnonymous = element.select("td:eq(4)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), 0));
            }
        }
        return proxyList;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public   void work() {
        Spider.create(new ProxyProcessor()).
                addUrl(ProxySite.sites[1]).
                addPipeline(proxyPipeline).
                        thread(1).
                run();
    }
}
