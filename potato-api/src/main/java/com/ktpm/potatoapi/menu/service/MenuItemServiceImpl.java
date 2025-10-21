package com.ktpm.potatoapi.menu.service;

import com.ktpm.potatoapi.category.entity.Category;
import com.ktpm.potatoapi.category.repo.CategoryRepository;
import com.ktpm.potatoapi.cloudinary.CloudinaryService;
import com.ktpm.potatoapi.common.exception.AppException;
import com.ktpm.potatoapi.common.exception.ErrorCode;
import com.ktpm.potatoapi.common.utils.SecurityUtils;
import com.ktpm.potatoapi.menu.dto.MenuItemDetailResponse;
import com.ktpm.potatoapi.menu.dto.MenuItemRequest;
import com.ktpm.potatoapi.menu.dto.MenuItemResponse;
import com.ktpm.potatoapi.menu.entity.MenuItem;
import com.ktpm.potatoapi.menu.mapper.MenuItemMapper;
import com.ktpm.potatoapi.menu.repo.MenuItemRepository;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.merchant.repo.MerchantRepository;
import com.ktpm.potatoapi.option.entity.Option;
import com.ktpm.potatoapi.option.repo.OptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {
    MenuItemMapper menuItemMapper;
    MenuItemRepository menuItemRepository;
    MerchantRepository merchantRepository;
    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;
    SecurityUtils securityUtils;
    OptionRepository optionRepository;

    @Override
    public List<MenuItemResponse> getAllMenuItemsOfMyMerchant() {
        Merchant merchant = securityUtils.getCurrentMerchant();

        log.info("Get all menu items of merchant {} for Merchant Admin", merchant.getName());

        return menuItemRepository.findAllByMerchantIdAndIsActiveTrue(merchant.getId())
                .stream()
                .map(menuItemMapper::toMenuItemResponse)
                .toList();
    }

    @Override
    public List<MenuItemResponse> getAllMenuItemsForCustomer(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        if (!merchant.isOpen())
            throw new AppException(ErrorCode.MERCHANT_CLOSED);

        log.info("Get all menu items of merchant {} for Customer", merchant.getName());

        return menuItemRepository.findAllByMerchantIdAndIsVisibleTrue(merchant.getId())
                .stream()
                .map(menuItemMapper::toMenuItemResponse)
                .toList();
    }

    @Override
    public MenuItemDetailResponse getMenuItemForMerAdmin(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findByIdAndIsActiveTrue(menuItemId)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

        MenuItemDetailResponse response = menuItemMapper.toMenuItemDetailResponse(menuItem);

        log.info("Get menu item {} successfully",menuItemId);

        return response;
    }

    @Override
    public void createMenuItem(MenuItemRequest menuItemRequest) {
        Category category = categoryRepository.findById(menuItemRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Merchant merchant = securityUtils.getCurrentMerchant();

        MenuItem menuItem = menuItemMapper.toEntity(menuItemRequest);
        menuItem.setImgUrl(uploadMenuItemImage(menuItemRequest.getImgFile(), menuItem.getName()));
        menuItem.setCategory(category);
        menuItem.setMerchant(merchant);

        try {
            menuItemRepository.save(menuItem);
            log.info("{} create item {} successfully", merchant.getName(), menuItem.getName());
        } catch (DataIntegrityViolationException e) {
            log.error("Menu item with name {} already exists in {}", menuItem.getName(), merchant.getName());
            throw new AppException(ErrorCode.MENU_ITEM_EXISTED);
        }
    }

    @Override
    public void updateMenuItem(Long menuItemId, MenuItemRequest menuItemRequest) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

        Category category = categoryRepository.findById(menuItemRequest.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Merchant merchant = securityUtils.getCurrentMerchant();

        // Check menu item must be owned of current merchant
        if(!menuItem.getMerchant().equals(merchant))
            throw new AppException(ErrorCode.MUST_BE_OWNED_OF_CURRENT_MERCHANT);

        menuItemMapper.updateMenuItem(menuItem, menuItemRequest);
        menuItem.setCategory(category);
        menuItem.setImgUrl(uploadMenuItemImage(menuItemRequest.getImgFile(), menuItem.getName()));
        menuItemRepository.save(menuItem);

        log.info("Update menu item {}", menuItem.getName());
    }

    @Override
    public void updateMenuItemVisibleStatus(Long menuItemId, boolean isVisible) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

        menuItem.setVisible(isVisible);
        menuItemRepository.save(menuItem);
    }

    // thiếu xóa món ăn thì xóa liên kết với option
    @Override
    public void deleteMenuItem(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new AppException(ErrorCode.MENU_ITEM_NOT_FOUND));

        Merchant merchant = securityUtils.getCurrentMerchant();

        // Check menu item must be owned of current merchant
        if(!menuItem.getMerchant().equals(merchant))
            throw new AppException(ErrorCode.MUST_BE_OWNED_OF_CURRENT_MERCHANT);

        // Remove the menu item from all associated options
        List<Option> options = optionRepository.findAllByMenuItemsContaining(menuItem);
        for (Option option : options) {
            option.getMenuItems().remove(menuItem);
        }
        optionRepository.saveAll(options);

        menuItem.setVisible(false);
        menuItem.setActive(false);
        menuItemRepository.save(menuItem);

        log.info("Delete menu item {}", menuItem.getName());
    }

    private String uploadMenuItemImage(MultipartFile file, String objectName) {
        return cloudinaryService.upload(file, "menu_items", objectName);
    }
}
