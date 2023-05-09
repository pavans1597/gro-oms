package com.groyyo.order.management.controller.internal;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.order.management.dto.request.BuyerRequestDto;
import com.groyyo.order.management.dto.response.BuyerResponseDto;
import com.groyyo.order.management.service.BuyerService;
import com.groyyo.order.management.service.ProductService;
import com.groyyo.order.management.service.SizeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("internal")
public class InternalController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private SizeService sizeService;


    @PostMapping("buyer/add")
    public ResponseDto<BuyerResponseDto> addBuyer(@RequestBody @Valid BuyerRequestDto buyerRequestDto) {

        log.info("Request received to add buyer: {}", buyerRequestDto);

        BuyerResponseDto buyerResponseDto = buyerService.addBuyer(buyerRequestDto);

        return Objects.isNull(buyerResponseDto) ? ResponseDto.failure("Unable to add buyer !!")
                : ResponseDto.success("Buyer added successfully !!", buyerResponseDto);
    }

    @PostMapping("product/add")
    public ResponseDto<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {

        log.info("Request received to add product: {}", productRequestDto);

        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);

        return Objects.isNull(productResponseDto) ? ResponseDto.failure("Unable to add product !!") : ResponseDto.success("Product added successfully !!", productResponseDto);
    }


    @PostMapping("size/add")
    public ResponseDto<SizeResponseDto> addSize(@RequestBody @Valid SizeRequestDto sizeRequestDto) {

        log.info("Request received to add size: {}", sizeRequestDto);

        SizeResponseDto sizeResponseDto = sizeService.addSize(sizeRequestDto);

        return Objects.isNull(sizeResponseDto) ? ResponseDto.failure("Unable to add size !!")
                : ResponseDto.success("Size added successfully !!", sizeResponseDto);
    }

}
