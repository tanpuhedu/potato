package com.ktpm.potatoapi.rating.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    @NotNull(message = "RATING_ORDER_NULL")
    Long orderId;

    @NotNull(message = "RATING_NULL")
    @Range(min = 1, max = 5, message = "RATING_OUT_OF_RANGE")
    Integer rating;
}
