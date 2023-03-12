package com.groyyo.order.management.dto.response;

import com.groyyo.core.master.dto.response.BaseResponseDto;
import com.groyyo.order.management.dto.request.ImageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class StyleDto extends BaseResponseDto {

	private String id;

	private String styleNumber;

	private ImageDto styleImage;

	private ImageDto cadImage;

	private String productId;

	private String productName;

	private String factoryId;

}
