package com.groyyo.order.service;


import java.util.List;

import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;


public interface ProductService {

    /**
     * @param status
     * @return
     */
    List<ProductResponseDto> getAllProducts(Boolean status);

    /**
     * @param id
     * @return
     */
    ProductResponseDto getProductById(String id);

    /**
     * @param productRequestDto
     * @return
     */
    ProductResponseDto addProduct(ProductRequestDto productRequestDto);

    /**
     * @param productRequestDto
     * @return
     */
    ProductResponseDto updateProduct(ProductRequestDto productRequestDto);

    /**
     * @param id
     * @param status
     * @return
     */
    ProductResponseDto activateDeactivateProduct(String id, boolean status);

    /**
     * @param productResponseDto
     */
    void consumeProduct(ProductResponseDto productResponseDto);
}
