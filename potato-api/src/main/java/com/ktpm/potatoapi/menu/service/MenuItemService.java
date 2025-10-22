package com.ktpm.potatoapi.menu.service;

import com.ktpm.potatoapi.menu.dto.MenuItemDetailResponse;
import com.ktpm.potatoapi.menu.dto.MenuItemRequest;
import com.ktpm.potatoapi.menu.dto.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    // services for Merchant Admin
    List<MenuItemResponse> getAllMenuItemsOfMyMerchant();
    void createMenuItem(MenuItemRequest menuItemRequest);
    void updateMenuItem(Long menuItemId,MenuItemRequest menuItemRequest);
    void updateMenuItemVisibleStatus(Long menuItemId, boolean isVisible);
    void deleteMenuItem(Long menuItemId);

    // mutual service
    MenuItemDetailResponse getMenuItem(Long menuItemId);

    // services for Customer
    List<MenuItemResponse> getAllMenuItemsForCustomer(Long merchantId);
}
