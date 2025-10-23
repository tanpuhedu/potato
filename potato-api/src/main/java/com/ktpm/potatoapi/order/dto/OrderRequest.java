package com.ktpm.potatoapi.order.dto;

import com.ktpm.potatoapi.cart.dto.CartItemRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank(message = "ORDER_FULL_NAME_BLANK")
    String fullName;

    @NotBlank(message = "ORDER_PHONE_BLANK")
    String phone;

    @NotBlank(message = "ORDER_DELIVERY_ADDRESS")
    String deliveryAddress;

    String note;

    @NotEmpty(message = "ORDER_CART_ITEMS_EMPTY")
    List<CartItemRequest> cartItems;
}