package com.ktpm.potatoapi.menu.repo;

import com.ktpm.potatoapi.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByMerchantIdAndIsActiveTrue(Long merchantId);
    List<MenuItem> findAllByMerchantIdAndIsVisibleTrue(Long merchantId);
    Optional<MenuItem> findByIdAndIsActiveTrue(Long menuItemId);
}
