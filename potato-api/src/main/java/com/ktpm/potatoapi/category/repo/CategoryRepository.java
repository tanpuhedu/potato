package com.ktpm.potatoapi.category.repo;

import com.ktpm.potatoapi.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
