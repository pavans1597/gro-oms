package com.groyyo.order.management.service;


import com.groyyo.order.management.dto.request.FabricRequestDto;
import com.groyyo.order.management.dto.response.FabricResponseDto;

import java.util.List;

public interface FabricService {

    List<FabricResponseDto> getAllFabrics(Boolean status);


    FabricResponseDto getFabricById(String id);


    FabricResponseDto addFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto updateFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto activateDeactivateFabric(String id, boolean status);


    void consumeFabric(FabricResponseDto fabricResponseDto);
}

