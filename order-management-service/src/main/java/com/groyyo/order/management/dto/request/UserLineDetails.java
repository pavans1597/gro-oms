package com.groyyo.order.management.dto.request;

import com.groyyo.core.dto.userservice.LineType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserLineDetails {

	private String userId;
	private String userName;
	private String lineId;
	private String lineName;
	private LineType lineType;

}
