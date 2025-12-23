package com.arya.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arya.order.Order;
import com.arya.order.feign.ProductFeignClient;
import com.arya.order.service.OrderService;
import com.arya.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private ProductFeignClient productFeignClient;


    @Override
    @SentinelResource(value = "createOrder",blockHandler = "createIrderFallbackHandler")
    public Order createOrder(Long productId, Long userId) {

        Product product = productFeignClient.getProductById(productId);

        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNickName("小明");
        order.setAddress("成都");
        // 商品列表
        order.setProducts(List.of(product));
        return order;
    }

    public Order createIrderFallbackHandler(Long productId, Long userId, BlockException exception){
        log.error("createOrder fallback ,productId: {},userId: {}",productId,userId,exception);
        return new Order(){{
           setId(0L);
           setTotalAmount(BigDecimal.ZERO);
           setNickName("未知用户");
           setAddress("默认地址");
        }};
    }

    /**
     * 通过配置中心获取一个服务实例，使用RestTemplate发送请求
     * @param produtId prodcutId
     * @return Product
     */
    private Product getProduct(Long produtId){
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance serviceInstance = instances.get(0);
        String url = "http://" + serviceInstance.getHost()
                + ":" + serviceInstance.getPort()
                + "/product/getProduct" + produtId;
        log.info("发送远程调用请求：{}",url);
        return restTemplate.getForObject(url, Product.class);
    }

    private Product getProductBalancerAnnotation(Long productId){
        String url = "http://service-product/product/getProduct" + productId;
        log.info("发送远程请求:{}",url);
        return restTemplate.getForObject(url, Product.class);
    }


    private Product getProductBalanced(Long productId){
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        String url = "http://" + choose.getHost() + ":" + choose.getPort() + "/product/getProduct/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        return restTemplate.getForObject(url,Product.class);
    }
}

