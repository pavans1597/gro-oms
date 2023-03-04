package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;

public interface SizeGroupService {


    List<SizeGroupResponseDto> getAllSizeGroups(Boolean status);


    SizeGroupResponseDto getSizeGroupById(String id);


    SizeGroupResponseDto addSizeGroup(SizeGroupRequestDto sizeGroupRequestDto);


    SizeGroupResponseDto updateSizeGroup(SizeGroupRequestDto sizeGroupRequestDto);


    SizeGroupResponseDto activateDeactivateSizeGroup(String id, boolean status);


    void consumeSizeGroup(SizeGroupResponseDto sizeGroupResponseDto);
}
