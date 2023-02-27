package com.groyyo.order.management.service.adapter;

import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.order.management.service.entity.SizeGroup;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class SizeGroupAdapter {

    public SizeGroup buildSizeGroupFromRequest(SizeGroupRequestDto sizeGroupRequestDto) {

        return SizeGroup
                .builder()
                .name(sizeGroupRequestDto.getName())
                .sizeIds(sizeGroupRequestDto.getSizeIds())
                .build();
    }

    public SizeGroup buildSizeGroupFromResponse(SizeGroupResponseDto sizeGroupResponseDto) {

        return SizeGroup
                .builder()
                .name(sizeGroupResponseDto.getName())
                .sizeIds(sizeGroupResponseDto.getSizeIds())
                .build();
    }

    @SuppressWarnings("unchecked")
    public SizeGroup cloneSizeGroupWithRequest(SizeGroupRequestDto sizeGroupRequestDto, SizeGroup sizeGroup) {

        if (StringUtils.isNotBlank(sizeGroupRequestDto.getName()))
            sizeGroup.setName(sizeGroupRequestDto.getName());

        if (CollectionUtils.isNotEmpty(sizeGroupRequestDto.getSizeIds())) {

            if (sizeGroupRequestDto.isReplaceCall())
                sizeGroup.setSizeIds(sizeGroupRequestDto.getSizeIds());
            else
                sizeGroup.setSizeIds((List<String>) CollectionUtils.union(sizeGroup.getSizeIds(), sizeGroupRequestDto.getSizeIds()));
        }

        return sizeGroup;
    }

    public SizeGroupResponseDto buildResponseFromEntity(SizeGroup sizeGroup) {

        return SizeGroupResponseDto
                .builder()
                .uuid(sizeGroup.getUuid())
                .name(sizeGroup.getName())
                .sizeIds(sizeGroup.getSizeIds())
                .status(sizeGroup.isStatus())
                .build();
    }

    public List<SizeGroupResponseDto> buildResponsesFromEntities(List<SizeGroup> sizeGroups) {

        return sizeGroups.stream().map(SizeGroupAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
