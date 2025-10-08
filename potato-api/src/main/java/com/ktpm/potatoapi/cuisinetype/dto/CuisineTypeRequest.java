package com.ktpm.potatoapi.cuisinetype.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CuisineTypeRequest {
    String name;
    MultipartFile imgFile;
}
