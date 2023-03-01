package com.groyyo.order.service;


import com.groyyo.order.dto.request.FabricRequestDto;
import com.groyyo.order.dto.response.FabricResponseDto;

import java.util.List;

public interface FabricService {

    List<FabricResponseDto> getAllFabrics(Boolean status);


    FabricResponseDto getFabricById(String id);


    FabricResponseDto addFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto updateFabric(FabricRequestDto fabricRequestDto);


    FabricResponseDto activateDeactivateFabric(String id, boolean status);


    void consumeFabric(FabricResponseDto fabricResponseDto);
}

