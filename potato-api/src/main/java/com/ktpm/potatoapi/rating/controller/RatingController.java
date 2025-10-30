package com.ktpm.potatoapi.rating.controller;

import com.ktpm.potatoapi.rating.dto.RatingRequest;
import com.ktpm.potatoapi.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rating Controller", description = "APIs for rating")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {
    RatingService ratingService;

    @PostMapping("/rating")
    @Operation(summary = "Rate an order",
            description = "API for Customer to rate an order")
    public ResponseEntity<?> rateOrder(@RequestBody @Valid RatingRequest ratingRequest) {
        ratingService.createRating(ratingRequest);
        return ResponseEntity.ok().build();
    }
}
