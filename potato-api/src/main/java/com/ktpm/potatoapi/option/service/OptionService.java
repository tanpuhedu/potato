package com.ktpm.potatoapi.option.service;

import com.ktpm.potatoapi.option.dto.OptionCreationRequest;
import com.ktpm.potatoapi.option.dto.OptionResponse;
import com.ktpm.potatoapi.option.dto.OptionUpdateRequest;
import com.ktpm.potatoapi.option.dto.OptionValueRequest;

import java.util.List;

public interface OptionService {
    // services for Merchant Admin
    List<OptionResponse> getAllOptionsOfMyMerchant();
    void createOptionAndOptionValue(OptionCreationRequest request);
    void createOptionValueForExistingOption(Long optionId, OptionValueRequest request);
    void updateOption(Long optionId, OptionUpdateRequest request);
    void updateOptionValue(Long id, OptionValueRequest request);
    void updateOptionValueVisibleStatus(Long valueId, boolean isVisible);
    void deleteOptionValue(Long valueId);
    void deleteOption(Long optionId);

    // services for Customer
    List<OptionResponse> getAllOptionsForCustomer(Long merchantId);
}
