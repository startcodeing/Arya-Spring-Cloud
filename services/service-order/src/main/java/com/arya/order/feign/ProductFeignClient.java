package com.arya.order.feign;

import com.arya.order.feign.fallback.ProductFeignClientFallBack;
import com.arya.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product",fallback = ProductFeignClientFallBack.class)
public interface ProductFeignClient {


    @GetMapping("product/getProduct/{id}")
    Product getProductById(@PathVariable Long id);
}
