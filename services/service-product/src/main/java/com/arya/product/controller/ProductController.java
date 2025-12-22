package com.arya.product.controller;

import com.arya.product.Product;
import com.arya.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查询商品
     */
    @GetMapping("/getProduct/{id}")
    public Product getProduct(@PathVariable Long id) {
        log.info("server-product 接收到请求：productId：{}", id);
        return productService.getProductById(id);
    }
}
