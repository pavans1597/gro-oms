package com.groyyo.order.management.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BulkPartRequestDto {
    private String name;

    private Double tolerance;

    private String sizeGroup;

    private List<String> sizes;

    private List<BulkColorRequestDto> colors;
}
