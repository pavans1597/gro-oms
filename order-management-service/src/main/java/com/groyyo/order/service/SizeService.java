package com.groyyo.order.service;

import java.util.List;

import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;

public interface SizeService {

    /**
     * @param status
     * @return
     */
    List<SizeResponseDto> getAllSizes(Boolean status);

    /**
     * @param id
     * @return
     */
    SizeResponseDto getSizeById(String id);

    /**
     * @param sizeRequestDto
     * @return
     */
    SizeResponseDto addSize(SizeRequestDto sizeRequestDto);

    /**
     * @param sizeRequestDto
     * @return
     */
    SizeResponseDto updateSize(SizeRequestDto sizeRequestDto);

    /**
     * @param id
     * @param status
     * @return
     */
    SizeResponseDto activateDeactivateSize(String id, boolean status);

    /**
     * @param sizeResponseDto
     */
    void consumeSize(SizeResponseDto sizeResponseDto);
}
