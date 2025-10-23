package com.ktpm.potatoapi.merchant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantRegistrationRequest {
    @NotBlank(message = "REGISTERED_MERCHANT_ADMIN_FULL_NAME_BLANK")
    String fullName;

    @NotBlank(message = "EMAIL_BLANK")
    @Email(message = "EMAIL_INVALID")
    String email;

    @NotBlank(message = "REGISTERED_MERCHANT_NAME_BLANK")
    String merchantName;

    @NotBlank(message = "ADDRESS_BLANK")
    String address;

    @NotEmpty(message = "CUISINE_TYPES_EMPTY")
    Set<String> cuisineTypes;
}