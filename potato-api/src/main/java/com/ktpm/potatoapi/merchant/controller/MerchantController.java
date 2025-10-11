package com.ktpm.potatoapi.merchant.controller;

import com.ktpm.potatoapi.merchant.dto.MerchantRegistrationRequest;
import com.ktpm.potatoapi.merchant.dto.MerchantUpdateRequest;
import com.ktpm.potatoapi.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/admin/merchants")
    @Operation(summary = "Show all merchants for System Admin",
            description = "API for System Admin to retrieve a list of all merchants")
    public ResponseEntity<?> getAllMerchantsForSysAdmin() {
        return ResponseEntity.ok(merchantService.getAllMerchantsForSysAdmin());
    }

    @GetMapping("/admin/merchants/{id}")
    @Operation(summary = "Show a merchant for System Admin",
            description = "API for System Admin to retrieve a specific merchant")
    public ResponseEntity<?> getMerchantForSysAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.getMerchantForSysAdmin(id));
    }

    @PatchMapping("/admin/merchants/{id}/isActive")
    @Operation(summary = "Change a merchant's active status",
            description = "API for System Admin to activate/deactivate a merchant")
    public ResponseEntity<?> updateMerchantActiveStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        merchantService.updateMerchantActiveStatus(id, isActive);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/merchant/my-merchant")
    @Operation(summary = "Show my merchant information",
            description = "API for Merchant Admin to retrieve their own merchant information")
    public ResponseEntity<?> getMyMerchant() {
        return ResponseEntity.ok(merchantService.getMyMerchant());
    }

    @PutMapping(value = "/merchant/my-merchant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update my merchant information",
            description = "API for Merchant Admin to update their own merchant information")
    public ResponseEntity<?> updateMyMerchant(
            @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart("data")
            @Valid MerchantUpdateRequest request,

            @RequestParam("img") MultipartFile imgFile
    ) {
        merchantService.updateMyMerchant(request, imgFile);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/merchant/my-merchant/isOpen")
    @Operation(summary = "Update my merchant's open status",
            description = "API for Merchant Admin to update their own merchant's open status")
    public ResponseEntity<?> updateMyMerchantOpenStatus(@RequestParam boolean isOpen) {
        merchantService.updateMyMerchantOpenStatus(isOpen);
        return ResponseEntity.ok().build();
    }
}