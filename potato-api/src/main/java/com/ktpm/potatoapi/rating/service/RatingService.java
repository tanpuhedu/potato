package com.ktpm.potatoapi.rating.service;

import com.ktpm.potatoapi.rating.dto.RatingRequest;

public interface RatingService {
    void createRating(RatingRequest ratingRequest);
}
