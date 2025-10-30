package com.ktpm.potatoapi.rating.repo;

import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.merchant = :merchant AND r.rating IS NOT NULL")
    BigDecimal calcAvgRatingByMerchant(@Param("merchant") Merchant merchant);
}
