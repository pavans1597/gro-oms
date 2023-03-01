package com.groyyo.order.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groyyo.order.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeDTO {

    @NotNull
    @JsonProperty("name")
    private String name;

    private String id;
    public static SizeDTO fromEntity(Size size) {
        return SizeDTO.builder()
                .name(size.getName())
                .id(size.getUuid())
                .build();
    }

}
