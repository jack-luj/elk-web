package com.github.jackl.elk;

import com.github.jackl.elk.utils.GitVer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by jackl on 2017/2/6.
 */
@Configuration("com.github.jackl.elk.simpleConfig")
@EnableAutoConfiguration
class SimpleConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisTemplate<String, Object> objectRedisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public GitVer getGitVer(){
        return new GitVer();
    }

}