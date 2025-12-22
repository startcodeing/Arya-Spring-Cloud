package com.arya.order.service;

import com.arya.order.Order;

public interface OrderService {

    Order createOrder(Long product, Long userId);
}
