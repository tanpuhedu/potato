package com.ktpm.potatoapi.auth.entity;

import lombok.Getter;

@Getter
public enum Role {
    SYSTEM_ADMIN,
    MERCHANT_ADMIN,
    CUSTOMER
}
