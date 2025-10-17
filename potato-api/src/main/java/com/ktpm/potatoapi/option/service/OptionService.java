package com.ktpm.potatoapi.option.service;

import com.ktpm.potatoapi.option.dto.*;

import java.util.List;

public interface OptionService {
    // services for Merchant Admin
    List<OptionResponse> getAllOptionsOfMyMerchant();
    OptionDetailResponse getOptionForMerAdmin(Long optionId);
    void createOptionAndOptionValue(OptionCreationRequest request);
    void createOptionValueForExistingOption(Long optionId, OptionValueRequest request);
    void updateOption(Long optionId, OptionUpdateRequest request);
    void updateOptionValue(Long id, OptionValueRequest request);
    void updateOptionValueVisibleStatus(Long valueId, boolean isVisible);
    void assignMenuItemToOption(Long optionId, AddMenuItemToOptionRequest request);
    void deleteOptionValue(Long valueId);
    void deleteOption(Long optionId);
}
