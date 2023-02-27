package com.groyyo.order.management.service.service;


import com.groyyo.order.management.service.dto.request.FabricRequestDto;
import com.groyyo.order.management.service.dto.response.FabricResponseDto;

import java.util.List;

public interface FabricService {

    List<FabricResponseDto> getAllFabrics(Boolean status);


    FabricResponseDto getFabricById(String id);


    FabricResponseDto addFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto updateFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto activateDeactivateFabric(String id, boolean status);


    void consumeFabric(FabricResponseDto fabricResponseDto);
}

