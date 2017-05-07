package com.github.jackl.elk;

import com.github.jackl.elk.utils.GitVer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Created by elk on 2017/5/7.
 */
@SpringBootApplication
@ServletComponentScan
public class Application implements CommandLineRunner {
    private Logger _logger= LoggerFactory.getLogger(Application.class);
    @Autowired
    private GitVer gitVer;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        _logger.info("elk-web启动成功."+gitVer.getVersion());
    }
}
