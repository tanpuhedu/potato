package com.ktpm.potatoapi.option.repo;

import com.ktpm.potatoapi.option.entity.OptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {
    Optional<OptionValue> findByIdAndIsActiveTrue(Long valueId);
}
