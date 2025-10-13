package com.ktpm.potatoapi.option.service;

import com.ktpm.potatoapi.option.dto.OptionCreationRequest;
import com.ktpm.potatoapi.option.dto.OptionResponse;
import com.ktpm.potatoapi.option.dto.OptionValueCreationRequest;

import java.util.List;

public interface OptionService {
    // services for Merchant Admin
    List<OptionResponse> getAllOptionsOfMyMerchant();
    void createOptionAndOptionValue(OptionCreationRequest request);
    void createOptionValueForExistingOption(Long optionId, OptionValueCreationRequest request);
    void updateOptionValueVisibleStatus(Long valueId, boolean isVisible);

    // services for Customer
    List<OptionResponse> getAllOptionsForCustomer(Long merchantId);
}
