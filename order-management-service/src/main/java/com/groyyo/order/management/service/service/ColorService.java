package com.groyyo.order.management.service.service;

import java.util.List;

import com.groyyo.core.master.dto.request.ColorRequestDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;

public interface ColorService {


    List<ColorResponseDto> getAllColors(Boolean status);


    ColorResponseDto getColorById(String id);


    ColorResponseDto addColor(ColorRequestDto colorRequestDto);


    ColorResponseDto updateColor(ColorRequestDto colorRequestDto);


    ColorResponseDto activateDeactivateColor(String id, boolean status);


    void consumeColor(ColorResponseDto colorResponseDto);
}
