package com.arya.order.feign.fallback;


import com.arya.order.feign.ProductFeignClient;
import com.arya.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


/**
 * FeignClient兜底回调，可以返回缓存，假数据，默认数据等，可以根据业务场景决定。
 */
@Component
@Slf4j
public class ProductFeignClientFallBack implements ProductFeignClient {

    @Override
    public Product getProductById(Long id) {
        log.info("ProductFeignClientFallback兜底数据-------------------");
        return new Product(){{
            setId(-1L);
            setProductName("ProductFeignClientFallback兜底数据");
            setNum(-1);
            setPrice(BigDecimal.ZERO);
        }};
    }
}
