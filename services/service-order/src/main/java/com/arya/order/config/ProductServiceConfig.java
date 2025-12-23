package com.arya.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductServiceConfig {


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    //OpenFegin客户端的日志级别
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }


    //OpenFegin默认的重启器，默认重试5次，每次的间隔时间为前一次时间的1.5倍，初始的间隔时间为100ms
    @Bean
    Retryer retryer(){
        return new Retryer.Default();
    }
}
