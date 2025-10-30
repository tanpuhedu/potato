package com.ktpm.potatoapi.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantUpdateRequest {
    @NotBlank(message = "INTRO_BLANK")
    String introduction;

    @NotBlank(message = "ADDRESS_BLANK")
    String address;

    @NotEmpty(message = "OPENING_HOURS_EMPTY")
    Map<String, String> openingHours;

    @NotEmpty(message = "CUISINE_TYPES_EMPTY")
    Set<String> cuisineTypes;
}