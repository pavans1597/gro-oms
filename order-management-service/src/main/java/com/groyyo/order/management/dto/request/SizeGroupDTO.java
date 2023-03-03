package com.groyyo.order.management.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groyyo.order.management.entity.SizeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeGroupDTO {

    @NotNull
    @Size(max = 255)
    @JsonProperty("name")
    private String name;


    @NotNull
    @JsonProperty("sizeIds")
    private List<String> sizeIds;

    private String id;
    public static SizeDTO fromEntity(SizeGroup sizeGroup) {
        return SizeDTO.builder()
                .name(sizeGroup.getName())
                .id(sizeGroup.getUuid())
                .build();
    }

}