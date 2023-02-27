package com.groyyo.order.management.service.service;

import java.util.List;

import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;

public interface SizeGroupService {

    /**
     * @param status
     * @return
     */
    List<SizeGroupResponseDto> getAllSizeGroups(Boolean status);

    /**
     * @param id
     * @return
     */
    SizeGroupResponseDto getSizeGroupById(String id);

    /**
     * @param sizeGroupRequestDto
     * @return
     */
    SizeGroupResponseDto addSizeGroup(SizeGroupRequestDto sizeGroupRequestDto);

    /**
     * @param sizeGroupRequestDto
     * @return
     */
    SizeGroupResponseDto updateSizeGroup(SizeGroupRequestDto sizeGroupRequestDto);

    /**
     * @param id
     * @param status
     * @return
     */
    SizeGroupResponseDto activateDeactivateSizeGroup(String id, boolean status);

    /**
     * @param sizeGroupResponseDto
     */
    void consumeSizeGroup(SizeGroupResponseDto sizeGroupResponseDto);
}
