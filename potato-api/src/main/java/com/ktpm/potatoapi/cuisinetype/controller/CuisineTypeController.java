package com.ktpm.potatoapi.cuisinetype.controller;

import com.ktpm.potatoapi.cuisinetype.dto.CuisineTypeRequest;
import com.ktpm.potatoapi.cuisinetype.service.CuisineTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cuisine Type Controller", description = "APIs for cuisine type module")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CuisineTypeController {
    CuisineTypeService cuisineTypeService;

    @GetMapping("/admin/cuisine-types")
    @Operation(summary = "Show all cuisine types in system",
            description = "API for System Admin to retrieve a list of all cuisine types")
    public ResponseEntity<?> getAllCuisineTypes() {
        return ResponseEntity.ok(cuisineTypeService.getAllCuisineTypes());
    }

    @GetMapping(path = {"/merchant/cuisine-types", "/cuisine-types"})
    @Operation(summary = "Show all visible cuisine types in system",
            description = "API for Merchant Admin or Customer to retrieve a list of all active cuisine types")
    public ResponseEntity<?> getAllVisibleCuisineTypes() {
        return ResponseEntity.ok(cuisineTypeService.getAllVisibleCuisineTypes());
    }

    @PostMapping(path = "/admin/cuisine-types", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new cuisine type",
            description = "API for System Admin to create a new cuisine type")
    public ResponseEntity<?> createCuisineType(@ModelAttribute CuisineTypeRequest request) {
        cuisineTypeService.createCuisineType(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/admin/cuisine-types/{id}/status")
    @Operation(summary = "Update a cuisine type's visible status",
            description = "API for System Admin to delete a cuisine type")
    public ResponseEntity<?> updateCuisineTypeStatus(@PathVariable Long id, @RequestParam boolean isVisible) {
        cuisineTypeService.updateCuisineTypeStatus(id, isVisible);
        return ResponseEntity.ok().build();
    }
}
