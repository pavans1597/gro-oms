package com.groyyo.order.management.adapter;

import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.order.management.entity.Product;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductAdapter {

	public Product buildProductFromRequest(ProductRequestDto productRequestDto,String factoryId) {

		return Product
				.builder()
				.name(productRequestDto.getName())
				.lineArt(productRequestDto.getLineArt())
				.masterId(productRequestDto.getMasterId())
				.factoryId(factoryId)
				.build();
	}

	public Product buildProductFromResponse(ProductResponseDto productResponseDto,String factoryId) {

		return Product
				.builder()
				.name(productResponseDto.getName())
				.lineArt(productResponseDto.getLineArt())
				.masterId(productResponseDto.getMasterId())
				.factoryId(factoryId)
				.build();
	}

	public Product cloneProductWithRequest(ProductRequestDto productRequestDto, Product product) {

		if (StringUtils.isNotBlank(productRequestDto.getName()))
			product.setName(productRequestDto.getName());

		if (StringUtils.isNotBlank(productRequestDto.getLineArt()))
			product.setLineArt(productRequestDto.getLineArt());

		if (StringUtils.isNotBlank(productRequestDto.getMasterId()))
			product.setMasterId(productRequestDto.getMasterId());

		return product;
	}

	public ProductRequestDto buildRequestFromResponse(ProductResponseDto productResponseDto) {

		ProductRequestDto productRequestDto = ProductRequestDto.builder().build();

		if (StringUtils.isNotBlank(productResponseDto.getName()))
			productRequestDto.setName(productResponseDto.getName());

		if (StringUtils.isNotBlank(productResponseDto.getMasterId()))
			productRequestDto.setMasterId(productResponseDto.getMasterId());

		return productRequestDto;
	}

	public ProductResponseDto buildResponseFromEntity(Product product) {

		return ProductResponseDto
				.builder()
				.uuid(product.getUuid())
				.name(product.getName())
				.lineArt(product.getLineArt())
				.masterId(product.getMasterId())
				.status(product.isStatus())
				.factoryId(product.getFactoryId())
				.build();
	}

	public List<ProductResponseDto> buildResponsesFromEntities(List<Product> products) {

		return products.stream().map(ProductAdapter::buildResponseFromEntity).collect(Collectors.toList());
	}

}
