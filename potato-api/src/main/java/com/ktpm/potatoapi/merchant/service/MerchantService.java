package com.ktpm.potatoapi.merchant.service;

import com.ktpm.potatoapi.merchant.dto.MerchantRegistrationRequest;
import com.ktpm.potatoapi.merchant.dto.MerchantRegistrationResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface MerchantService {
    // services for merchant on-boarding
    List<MerchantRegistrationResponse> getAllRegisteredMerchants();
    void registerMerchant(MerchantRegistrationRequest request);
    void approveMerchant(Long id) throws MessagingException;
}