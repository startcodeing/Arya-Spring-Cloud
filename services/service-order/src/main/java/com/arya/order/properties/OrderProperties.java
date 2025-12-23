package com.arya.order.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 替代使用@RefreshScope+@Value注解的方式，实现批量配置的绑定
 */
@Data
@Component
@ConfigurationProperties(prefix = "order")
public class OrderProperties {

    private String timeout;
    private String autoConfirm;
    private String dbUrl;
}
