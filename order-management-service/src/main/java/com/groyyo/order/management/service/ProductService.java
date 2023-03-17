package com.groyyo.order.management.service;

import java.util.List;
import java.util.Map;

import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.order.management.entity.Product;

public interface ProductService {

	List<ProductResponseDto> getAllProducts(Boolean status);

	ProductResponseDto getProductById(String id);

	ProductResponseDto addProduct(ProductRequestDto productRequestDto);

	ProductResponseDto updateProduct(ProductRequestDto productRequestDto);

	ProductResponseDto activateDeactivateProduct(String id, boolean status);

	void consumeProduct(ProductResponseDto productResponseDto);

	/**
	 * @param productByNameMap
	 */
	void saveEntityFromCache(Map<String, ProductResponseDto> productByNameMap);

	Product findOrCreate(String name);

	/**
	 * @param productResponseDto
	 * @return
	 */
	ProductResponseDto conditionalSaveProduct(ProductResponseDto productResponseDto);
}
