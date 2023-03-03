package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;

public interface SizeService {


    List<SizeResponseDto> getAllSizes(Boolean status);


    SizeResponseDto getSizeById(String id);


    SizeResponseDto addSize(SizeRequestDto sizeRequestDto);


    SizeResponseDto updateSize(SizeRequestDto sizeRequestDto);


    SizeResponseDto activateDeactivateSize(String id, boolean status);


    void consumeSize(SizeResponseDto sizeResponseDto);
}
