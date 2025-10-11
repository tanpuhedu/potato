package com.ktpm.potatoapi.category.service;

import com.ktpm.potatoapi.category.dto.CategoryRequest;
import com.ktpm.potatoapi.category.dto.CategoryResponse;
import com.ktpm.potatoapi.category.entity.Category;
import com.ktpm.potatoapi.category.mapper.CategoryMapper;
import com.ktpm.potatoapi.category.repo.CategoryRepository;
import com.ktpm.potatoapi.common.exception.AppException;
import com.ktpm.potatoapi.common.exception.ErrorCode;
import com.ktpm.potatoapi.common.utils.SecurityUtils;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.merchant.repo.MerchantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    SecurityUtils securityUtils;
    MerchantRepository merchantRepository;

    @Override
    public List<CategoryResponse> getAllCategoriesOfMyMerchant() {
        log.info("Get all categories for Merchant Admin");
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getAllCategoriesForCustomer(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new AppException(ErrorCode.MERCHANT_NOT_FOUND));

        if (!merchant.isOpen())
            throw new AppException(ErrorCode.MERCHANT_CLOSED);

        log.info("Get all categories for Customer");

        return categoryRepository.findAllByMerchantIdAndIsActiveTrue(merchantId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void createCategory(CategoryRequest categoryRequest) {
        Merchant merchant = securityUtils.getCurrentMerchant();

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setMerchant(merchant);

        try {
            categoryRepository.save(category);
            log.info("Create category {}", category.getName());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
    }

    @Override
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Merchant merchant = securityUtils.getCurrentMerchant();

        // Check category must be owned of current merchant
        if (!category.getMerchant().equals(merchant))
            throw new AppException(ErrorCode.MUST_BE_OWNED_OF_CURRENT_MERCHANT);

        category.setName(categoryRequest.getName());

        try {
            categoryRepository.save(category);
            log.info("{} update category: {}", merchant.getName(), category.getName());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Merchant merchant = securityUtils.getCurrentMerchant();

        // Check category must be owned by current merchant
        if (!category.getMerchant().equals(merchant))
            throw new AppException(ErrorCode.MUST_BE_OWNED_OF_CURRENT_MERCHANT);

        category.setActive(false);
        categoryRepository.save(category);

        log.info("{} delete {}", merchant.getName(), category.getName());
    }
}
