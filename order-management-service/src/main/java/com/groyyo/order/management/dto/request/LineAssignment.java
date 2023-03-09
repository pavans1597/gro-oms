package com.groyyo.order.management.dto.request;

import com.groyyo.order.management.entity.LineType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LineAssignment  {

    String userId ;
    String userName;
    String lineId ;
    private LineType lineType;

}
