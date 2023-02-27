package com.groyyo.order.management.service.adapter;

import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.order.management.service.entity.Product;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class ProductAdapter {

    public Product buildProductFromRequest(ProductRequestDto productRequestDto) {

        return Product
                .builder()
                .name(productRequestDto.getName())
                .lineArt(productRequestDto.getLineArt())
                .build();
    }

    public Product buildProductFromResponse(ProductResponseDto productResponseDto) {

        return Product
                .builder()
                .name(productResponseDto.getName())
                .lineArt(productResponseDto.getLineArt())
                .build();
    }

    public Product cloneProductWithRequest(ProductRequestDto productRequestDto, Product product) {

        if (StringUtils.isNotBlank(productRequestDto.getName()))
            product.setName(productRequestDto.getName());
        if (StringUtils.isNotBlank(productRequestDto.getLineArt()))
            product.setLineArt(productRequestDto.getLineArt());

        return product;
    }

    public ProductResponseDto buildResponseFromEntity(Product product) {

        return ProductResponseDto
                .builder()
                .uuid(product.getUuid())
                .name(product.getName())
                .lineArt(product.getLineArt())
                .status(product.isStatus())
                .build();
    }

    public List<ProductResponseDto> buildResponsesFromEntities(List<Product> products) {

        return products.stream().map(ProductAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
