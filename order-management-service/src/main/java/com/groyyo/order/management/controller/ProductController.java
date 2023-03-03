package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.groyyo.order.management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 */
@Log4j2
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("get/all")
    public ResponseDto<List<ProductResponseDto>> getAllProducts(@RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all products");

        List<ProductResponseDto> productResponseDtos = productService.getAllProducts(status);

        return ResponseDto.success("Found " + productResponseDtos.size() + " products in the system", productResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<ProductResponseDto> getProduct(@PathVariable String id) {

        log.info("Request received to get product with id: {}", id);

        ProductResponseDto productResponseDto = productService.getProductById(id);

        return Objects.isNull(productResponseDto) ? ResponseDto.failure("Found no product with id: " + id) : ResponseDto.success("Found product with id: " + id, productResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {

        log.info("Request received to add product: {}", productRequestDto);

        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);

        return Objects.isNull(productResponseDto) ? ResponseDto.failure("Unable to add product !!") : ResponseDto.success("Product added successfully !!", productResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<ProductResponseDto> updateProduct(@RequestBody ProductRequestDto productRequestDto) {

        log.info("Request received to update product: {}", productRequestDto);

        ProductResponseDto productResponseDto = productService.updateProduct(productRequestDto);

        return Objects.isNull(productResponseDto) ? ResponseDto.failure("Unable to update product !!") : ResponseDto.success("Product updated successfully !!", productResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<ProductResponseDto> activateDeactivateProduct(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate product with id: {}", id);

        ProductResponseDto productResponseDto = productService.activateDeactivateProduct(id, status);

        return Objects.isNull(productResponseDto) ? ResponseDto.failure("Found no product with id: " + id) : ResponseDto.success("Activated / Deactivated product with id: " + id, productResponseDto);
    }
}

