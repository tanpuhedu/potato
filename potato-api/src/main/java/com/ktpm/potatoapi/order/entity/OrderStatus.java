package com.ktpm.potatoapi.order.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    CONFIRMED (0),      // Khách vừa tạo đơn
    CANCELED (1),      // Merchant hủy
    DELIVERING (1),     // Chuẩn bị xong món và giao đơn cho tài xế nội bộ
    COMPLETED (2),      // Đơn giao tới khách hàng thành công
    ;
    Integer level;
}
