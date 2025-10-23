package com.ktpm.potatoapi.order.controller;

import com.ktpm.potatoapi.order.dto.OrderRequest;
import com.ktpm.potatoapi.order.dto.OrderStatusUpdateRequest;
import com.ktpm.potatoapi.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "APIs for order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/check-out")
    @Operation(summary = "Create a new order",
            description = "API for Customer to create a order")
    public ResponseEntity<?> checkout(@RequestBody @Valid OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Show all orders of Customer",
            description = "API for Customer to retrieve a list of orders")
    public ResponseEntity<?> getAllOrdersOfCustomer() {
        return ResponseEntity.ok(orderService.getAllOrdersOfCustomer());
    }

    @GetMapping("/merchant/my-orders")
    @Operation(summary = "Show all orders of Merchant",
            description = "API for Merchant Admin to retrieve a list of all orders from Customer")
    public ResponseEntity<?> getAllOrdersOfMerchant() {
        return ResponseEntity.ok(orderService.getAllOrdersOfMyMerchant());
    }

    @GetMapping(path = {"/orders/{orderId}", "merchant/orders/{orderId}"})
    @Operation(summary = "Show detail of order",
            description = "API for both Customer and Merchant Admin to show detail of an order")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @PatchMapping("/merchant/order/{orderId}")
    @Operation(summary = "Update status of an order",
            description = "API for Merchant Admin to update status of an order")
    public ResponseEntity<?> updateStatusOfOrder(@PathVariable Long orderId,
                                                 @RequestBody @Valid OrderStatusUpdateRequest request) {
        orderService.updateStatusOrder(orderId, request);
        return ResponseEntity.ok().build();
    }
}
