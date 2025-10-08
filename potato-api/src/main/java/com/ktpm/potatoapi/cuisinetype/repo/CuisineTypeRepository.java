package com.ktpm.potatoapi.cuisinetype.repo;

import com.ktpm.potatoapi.cuisinetype.entity.CuisineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuisineTypeRepository extends JpaRepository<CuisineType, Long> {
    List<CuisineType> findAllByIsVisibleTrue();
}
