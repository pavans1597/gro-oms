package com.groyyo.order.management.service.adapter;


import com.groyyo.order.management.service.dto.request.FabricRequestDto;
import com.groyyo.order.management.service.dto.response.FabricResponseDto;
import com.groyyo.order.management.service.entity.Fabric;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class FabricAdapter {

    public Fabric buildFabricFromRequest(FabricRequestDto fabricRequestDto) {

        return Fabric
                .builder()
                .name(fabricRequestDto.getName())
                .fabricCategory(fabricRequestDto.getFabricCategory())
                .imageUrl(fabricRequestDto.getImageUrl())
                .fabricCode(fabricRequestDto.getFabricCode())
                .build();
    }

    public Fabric buildFabricFromResponse(FabricResponseDto fabricResponseDto) {

        return Fabric
                .builder()
                .name(fabricResponseDto.getName())
                .fabricCategory(fabricResponseDto.getFabricCategory())
                .imageUrl(fabricResponseDto.getImageUrl())
                .fabricCode(fabricResponseDto.getFabricCode())
                .build();
    }

    public Fabric cloneFabricWithRequest(FabricRequestDto fabricRequestDto, Fabric fabric) {

        if (StringUtils.isNotBlank(fabricRequestDto.getName()))
            fabric.setName(fabricRequestDto.getName());

        if (StringUtils.isNotBlank(fabricRequestDto.getFabricCategory()))
            fabric.setFabricCategory(fabricRequestDto.getFabricCategory());

        if (StringUtils.isNotBlank(fabricRequestDto.getImageUrl()))
            fabric.setImageUrl(fabricRequestDto.getImageUrl());

        if (StringUtils.isNotBlank(fabricRequestDto.getFabricCode()))
            fabric.setFabricCode(fabricRequestDto.getFabricCode());

        return fabric;
    }

    public FabricRequestDto buildRequestFromResponse(FabricResponseDto fabricResponseDto) {

        FabricRequestDto fabricRequestDto = FabricRequestDto.builder().build();

        if (StringUtils.isNotBlank(fabricResponseDto.getName()))
            fabricRequestDto.setName(fabricResponseDto.getName());
        if (StringUtils.isNotBlank(fabricResponseDto.getFabricCategory()))
            fabricRequestDto.setFabricCategory(fabricResponseDto.getFabricCategory());
        if (StringUtils.isNotBlank(fabricResponseDto.getImageUrl()))
            fabricRequestDto.setImageUrl(fabricResponseDto.getImageUrl());
        if (StringUtils.isNotBlank(fabricResponseDto.getFabricCode()))
            fabricRequestDto.setFabricCode(fabricResponseDto.getFabricCode());

        return fabricRequestDto;
    }

    public FabricResponseDto buildResponseFromEntity(Fabric fabric) {

        return FabricResponseDto
                .builder()
                .uuid(fabric.getUuid())
                .name(fabric.getName())
                .fabricCategory(fabric.getFabricCategory())
                .imageUrl(fabric.getImageUrl())
                .fabricCode(fabric.getFabricCode())
                .status(fabric.isStatus())
                .build();
    }

    public List<FabricResponseDto> buildResponsesFromEntities(List<Fabric> fabrics) {

        return fabrics.stream().map(FabricAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}

