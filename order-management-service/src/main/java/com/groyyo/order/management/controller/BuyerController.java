package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.groyyo.order.management.dto.request.BuyerRequestDto;
import com.groyyo.order.management.dto.response.BuyerResponseDto;
import com.groyyo.order.management.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groyyo.core.base.common.dto.ResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("buyer")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @GetMapping("/get/all")
    public ResponseDto<List<BuyerResponseDto>> getAllBuyers(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all buyers");

        List<BuyerResponseDto> buyerResponseDtos = buyerService.getAllBuyers(status);

        return ResponseDto.success("Found " + buyerResponseDtos.size() + " buyers in the system", buyerResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<BuyerResponseDto> getBuyer(@PathVariable String id) {

        log.info("Request received to get buyer with id: {}", id);

        BuyerResponseDto buyerResponseDto = buyerService.getBuyerById(id);

        return Objects.isNull(buyerResponseDto) ? ResponseDto.failure("Found no buyer with id: " + id)
                : ResponseDto.success("Found buyer with id: " + id, buyerResponseDto);
    }

    @PostMapping("/add")
    public ResponseDto<BuyerResponseDto> addBuyer(@RequestBody @Valid BuyerRequestDto buyerRequestDto) {

        log.info("Request received to add buyer: {}", buyerRequestDto);

        BuyerResponseDto buyerResponseDto = buyerService.addBuyer(buyerRequestDto);

        return Objects.isNull(buyerResponseDto) ? ResponseDto.failure("Unable to add buyer !!")
                : ResponseDto.success("Buyer added successfully !!", buyerResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<BuyerResponseDto> updateBuyer(@RequestBody BuyerRequestDto buyerRequestDto) {

        log.info("Request received to update buyer: {}", buyerRequestDto);

        BuyerResponseDto buyerResponseDto = buyerService.updateBuyer(buyerRequestDto);

        return Objects.isNull(buyerResponseDto) ? ResponseDto.failure("Unable to update buyer !!")
                : ResponseDto.success("Buyer updated successfully !!", buyerResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<BuyerResponseDto> activateDeactivateBuyer(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate buyer with id: {}", id);

        BuyerResponseDto buyerResponseDto = buyerService.activateDeactivateBuyer(id, status);

        return Objects.isNull(buyerResponseDto) ? ResponseDto.failure("Found no buyer with id: " + id)
                : ResponseDto.success("Activated / Deactivated buyer with id: " + id, buyerResponseDto);
    }
}
