package com.ktpm.potatoapi.option.repo;

import com.ktpm.potatoapi.option.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByMerchantIdAndIsVisibleTrue(Long merchantId);
    List<Option> findAllByMerchantIdAndIsActiveTrue(Long merchantId);
}
