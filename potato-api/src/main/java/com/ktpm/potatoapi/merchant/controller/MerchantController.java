package com.ktpm.potatoapi.merchant.controller;

import com.ktpm.potatoapi.merchant.dto.MerchantRegistrationRequest;
import com.ktpm.potatoapi.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Merchant APIs", description = "APIs for merchant")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantController {
    MerchantService merchantService;

    @GetMapping("/admin/registered-merchants")
    @Operation(summary = "Show all registered merchants in system",
            description = "API for System Admin to retrieve a list of all registered merchants")
    public ResponseEntity<?> getAllRegisteredMerchants() {
        return ResponseEntity.ok(merchantService.getAllRegisteredMerchants());
    }

    @PostMapping("/merchant/register")
    @Operation(summary = "Register a business",
            description = "API for Merchant Admin to register a business")
    public ResponseEntity<?> registerMerchant(@RequestBody @Valid MerchantRegistrationRequest request) {
        merchantService.registerMerchant(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/registered-merchants/{id}/approve")
    @Operation(summary = "Approve a registered merchant",
            description = "API for System Admin to approve a registered merchant")
    public ResponseEntity<?> approveMerchant(@PathVariable Long id) throws MessagingException {
        merchantService.approveMerchant(id);
        return ResponseEntity.ok().build();
    }
}