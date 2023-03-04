package com.groyyo.order.management.service;


import java.util.List;

import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;


public interface ProductService {


    List<ProductResponseDto> getAllProducts(Boolean status);


    ProductResponseDto getProductById(String id);


    ProductResponseDto addProduct(ProductRequestDto productRequestDto);


    ProductResponseDto updateProduct(ProductRequestDto productRequestDto);


    ProductResponseDto activateDeactivateProduct(String id, boolean status);


    void consumeProduct(ProductResponseDto productResponseDto);
}
