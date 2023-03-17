package com.groyyo.order.management.service;

import java.util.List;

import com.groyyo.core.dto.PurchaseOrder.StyleDto;
import com.groyyo.order.management.entity.Product;
import com.groyyo.order.management.entity.Style;

public interface StyleService {

	List<StyleDto> getAllStyles(Boolean status);

	StyleDto getStyleById(String id);

	StyleDto addStyle(StyleDto styleRequestDto);

//    StyleDto addBulkStyle(List<StyleDto> styleRequestDto);

    StyleDto updateStyle(StyleDto styleUpdateDto);

	StyleDto activateDeactivateStyle(String id, boolean status);

	void consumeStyle(StyleDto styleDto);

	Style findOrCreate(String name, String styleNumber, Product product);
}
