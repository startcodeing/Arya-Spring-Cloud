package com.arya.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.arya.order.Order;
import com.arya.order.properties.OrderProperties;
import com.arya.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//RefreshScope + Value注解确保配置自动刷新，这种方式比较麻烦，可使用ConfigurationProperties(Prefix="order")的方式批量绑定
//@RefreshScope
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProperties orderProperties;

    //    @Value("${order.timeout}")
    //    String orderTimeout;
    //
    //    @Value("${order.auto-confirm}")
    //    String orderAutoConfirm;


    @GetMapping("/getConfig")
    public String getConfig() {
        //return "orderTimeout :" + orderTimeout + ",orderAutoConfirm:" + orderAutoConfirm;
        return "orderTimeout: " + orderProperties.getTimeout() +
                ", orderAutoConfirm: " + orderProperties.getAutoConfirm() +
                ", dbUrl: " + orderProperties.getDbUrl();
    }

    /**
     * 创建订单
     */
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    /**
     * 创建订单-秒杀
     */
    @GetMapping("/create/kill")
    // 自定义流控埋点
    @SentinelResource(value = "kill-order", fallback = "killOrderFallback")
    public Order createKillOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    public Order killOrderFallback(Long userId, Long productId, Throwable e) {
        log.info("killOrderFallback,userId:{},productId:{}", userId, productId, e);
        Order order = new Order() {{
            setId(-1L);
            setAddress("商品已下架");
        }};

        return order;
    }

    /**
     * 写数据
     */
    @GetMapping("/write")
    public String writeData() {
        return "write data success";
    }

    /**
     * 读数据
     */
    @GetMapping("/read")
    public String readData() {
        log.info("read success......");
        return "read data success";
    }


}
