package com.groyyo.order.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groyyo.core.dto.userservice.LineType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineAndColourResponse {

    private String LineId;
    private LineType lineType;
    private Set<String> coloursAssigned;
}
