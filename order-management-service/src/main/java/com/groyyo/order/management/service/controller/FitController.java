package com.groyyo.order.management.service.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

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
import com.groyyo.core.master.dto.request.FitRequestDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.order.management.service.service.FitService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("fit")
public class FitController {

    @Autowired
    private FitService fitService;

    @GetMapping("get/all")
    public ResponseDto<List<FitResponseDto>> getAllFits(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all fits");

        List<FitResponseDto> fitResponseDtos = fitService.getAllFits(status);

        return ResponseDto.success("Found " + fitResponseDtos.size() + " fits in the system", fitResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<FitResponseDto> getFit(@PathVariable String id) {

        log.info("Request received to get fit with id: {}", id);

        FitResponseDto fitResponseDto = fitService.getFitById(id);

        return Objects.isNull(fitResponseDto) ? ResponseDto.failure("Found no fit with id: " + id)
                : ResponseDto.success("Found fit with id: " + id, fitResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<FitResponseDto> addFit(@RequestBody @Valid FitRequestDto fitRequestDto) {

        log.info("Request received to add fit: {}", fitRequestDto);

        FitResponseDto fitResponseDto = fitService.addFit(fitRequestDto);

        return Objects.isNull(fitResponseDto) ? ResponseDto.failure("Unable to add fit !!")
                : ResponseDto.success("Fit added successfully !!", fitResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<FitResponseDto> updateFit(@RequestBody FitRequestDto fitRequestDto) {

        log.info("Request received to update fit: {}", fitRequestDto);

        FitResponseDto fitResponseDto = fitService.updateFit(fitRequestDto);

        return Objects.isNull(fitResponseDto) ? ResponseDto.failure("Unable to update fit !!")
                : ResponseDto.success("Fit updated successfully !!", fitResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<FitResponseDto> activateDeactivateFit(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate fit with id: {}", id);

        FitResponseDto fitResponseDto = fitService.activateDeactivateFit(id, status);

        return Objects.isNull(fitResponseDto) ? ResponseDto.failure("Found no fit with id: " + id)
                : ResponseDto.success("Activated / Deactivated fit with id: " + id, fitResponseDto);
    }
}
