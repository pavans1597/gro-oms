package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.dto.PurchaseOrder.StyleDto;

public interface StyleService {

	List<StyleDto> getAllStyles(Boolean status);

	StyleDto getStyleById(String id);

	StyleDto addStyle(StyleDto styleRequestDto);

	StyleDto updateStyle(StyleDto styleUpdateDto);

	StyleDto activateDeactivateStyle(String id, boolean status);

	void consumeStyle(StyleDto styleDto);
}
