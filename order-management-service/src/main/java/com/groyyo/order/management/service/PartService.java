package com.groyyo.order.management.service;


import java.util.List;

import com.groyyo.core.master.dto.request.PartRequestDto;
import com.groyyo.core.master.dto.response.PartResponseDto;


public interface PartService {

    /**
     * @param status
     * @return
     */
    List<PartResponseDto> getAllParts(Boolean status);

    /**
     * @param id
     * @return
     */
    PartResponseDto getPartById(String id);

    /**
     * @param partRequestDto
     * @return
     */
    PartResponseDto addPart(PartRequestDto partRequestDto);

    /**
     * @param partRequestDto
     * @return
     */
    PartResponseDto updatePart(PartRequestDto partRequestDto);

    /**
     * @param id
     * @param status
     * @return
     */
    PartResponseDto activateDeactivatePart(String id, boolean status);

    /**
     * @param partResponseDto
     */
    void consumePart(PartResponseDto partResponseDto);
}

