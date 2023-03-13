package com.groyyo.order.management.dto.request.dashboarddtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)public class AlterationCountResponseDto {
	private long totalAlteration;
	private long yetToStart;
	private long completed;
	private long inProgress;
}

