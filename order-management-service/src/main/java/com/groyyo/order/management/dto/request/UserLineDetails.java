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

	String userId;
	String userName;
	String lineId;
	String lineName;
	private LineType lineType;

}
