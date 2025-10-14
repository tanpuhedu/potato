package com.ktpm.potatoapi.option.repo;

import com.ktpm.potatoapi.option.entity.OptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {
}
