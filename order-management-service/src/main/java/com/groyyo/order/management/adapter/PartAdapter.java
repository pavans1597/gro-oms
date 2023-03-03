package com.groyyo.order.management.adapter;

import com.groyyo.core.master.dto.request.PartRequestDto;
import com.groyyo.core.master.dto.response.PartResponseDto;
import com.groyyo.order.management.entity.Part;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class PartAdapter {

    public Part buildPartFromRequest(PartRequestDto partRequestDto) {

        return Part
                .builder()
                .name(partRequestDto.getName())
                .build();
    }

    public Part buildPartFromResponse(PartResponseDto partResponseDto) {

        return Part
                .builder()
                .name(partResponseDto.getName())
                .build();
    }

    public Part clonePartWithRequest(PartRequestDto partRequestDto, Part part) {

        if (StringUtils.isNotBlank(partRequestDto.getName()))
            part.setName(partRequestDto.getName());

        return part;
    }

    public PartResponseDto buildResponseFromEntity(Part part) {

        return PartResponseDto
                .builder()
                .uuid(part.getUuid())
                .name(part.getName())
                .status(part.isStatus())
                .build();
    }

    public List<PartResponseDto> buildResponsesFromEntities(List<Part> parts) {

        return parts.stream().map(PartAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
