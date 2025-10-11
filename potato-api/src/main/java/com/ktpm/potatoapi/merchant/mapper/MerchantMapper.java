package com.ktpm.potatoapi.merchant.mapper;

import com.ktpm.potatoapi.merchant.dto.MerchantResponse;
import com.ktpm.potatoapi.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantMapper {
    @Mapping(target = "cuisineTypes", ignore = true)
    MerchantResponse toResponse(Merchant entity);
}