package com.groyyo.order.management.adapter;


import com.groyyo.order.management.dto.request.FabricCategoryRequestDto;
import com.groyyo.order.management.dto.response.FabricCategoryResponseDto;
import com.groyyo.order.management.entity.FabricCategory;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class FabricCategoryAdapter {

    public FabricCategory buildFabricCategoryFromRequest(FabricCategoryRequestDto fabricCategoryRequestDto,String factoryId) {

        return FabricCategory
                .builder()
                .name(fabricCategoryRequestDto.getName())
                .type(fabricCategoryRequestDto.getType())
                .factoryId(factoryId)
                .build();
    }

    public FabricCategory buildFabricCategoryFromResponse(FabricCategoryResponseDto fabricCategoryResponseDto,String factoryId) {

        return FabricCategory
                .builder()
                .name(fabricCategoryResponseDto.getName())
                .type(fabricCategoryResponseDto.getType())
                .factoryId(factoryId)
                .build();
    }

    public FabricCategory cloneFabricCategoryWithRequest(FabricCategoryRequestDto fabricCategoryRequestDto, FabricCategory fabricCategory) {

        if (StringUtils.isNotBlank(fabricCategoryRequestDto.getName()))
            fabricCategory.setName(fabricCategoryRequestDto.getName());

        if (StringUtils.isNotBlank(fabricCategoryRequestDto.getType()))
            fabricCategory.setType(fabricCategoryRequestDto.getType());

        return fabricCategory;
    }

    public FabricCategoryRequestDto buildRequestFromResponse(FabricCategoryResponseDto fabricCategoryResponseDto) {

        FabricCategoryRequestDto fabricCategoryRequestDto = FabricCategoryRequestDto.builder().build();

        if (StringUtils.isNotBlank(fabricCategoryResponseDto.getName()))
            fabricCategoryRequestDto.setName(fabricCategoryResponseDto.getName());
        if (StringUtils.isNotBlank(fabricCategoryResponseDto.getType()))
            fabricCategoryRequestDto.setType(fabricCategoryResponseDto.getType());

        return fabricCategoryRequestDto;
    }

    public FabricCategoryResponseDto buildResponseFromEntity(FabricCategory fabricCategory) {

        return FabricCategoryResponseDto
                .builder()
                .uuid(fabricCategory.getUuid())
                .name(fabricCategory.getName())
                .type(fabricCategory.getType())
                .status(fabricCategory.isStatus())
                .build();
    }

    public List<FabricCategoryResponseDto> buildResponsesFromEntities(List<FabricCategory> fabricCategorys) {

        return fabricCategorys.stream().map(FabricCategoryAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}

