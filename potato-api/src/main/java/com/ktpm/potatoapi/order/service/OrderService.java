package com.ktpm.potatoapi.order.service;

import com.ktpm.potatoapi.order.dto.OrderResponse;
import com.ktpm.potatoapi.order.dto.OrderRequest;

import java.util.List;

public interface OrderService {
    // services for customer
    void createOrder(OrderRequest cartOrderRequest);
    List<OrderResponse> getAllOrdersOfCustomer();
}
