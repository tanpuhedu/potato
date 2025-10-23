package com.ktpm.potatoapi.merchant.repo;

import com.ktpm.potatoapi.merchant.entity.RegisteredMerchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredMerchantRepository extends JpaRepository<RegisteredMerchant, Long> {
    boolean existsByMerchantName(String merchantName);
}
