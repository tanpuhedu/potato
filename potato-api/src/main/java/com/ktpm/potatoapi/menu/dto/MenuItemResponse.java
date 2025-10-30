package com.ktpm.potatoapi.menu.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemResponse {
    Long id;
    String name;
    Long basePrice;
    String imgUrl;
    boolean isActive;
    boolean isVisible;
}
