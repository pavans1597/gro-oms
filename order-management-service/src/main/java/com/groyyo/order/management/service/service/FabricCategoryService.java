package com.groyyo.order.management.service.service;


import com.groyyo.order.management.service.dto.request.FabricCategoryRequestDto;
import com.groyyo.order.management.service.dto.response.FabricCategoryResponseDto;

import java.util.List;

public interface FabricCategoryService {

    List<FabricCategoryResponseDto> getAllFabricCategorys(Boolean status);


    FabricCategoryResponseDto getFabricCategoryById(String id);


    FabricCategoryResponseDto addFabricCategory(FabricCategoryRequestDto fabricRequestDto);


    FabricCategoryResponseDto updateFabricCategory(FabricCategoryRequestDto fabricRequestDto);


    FabricCategoryResponseDto activateDeactivateFabricCategory(String id, boolean status);


    void consumeFabricCategory(FabricCategoryResponseDto fabricResponseDto);
}

