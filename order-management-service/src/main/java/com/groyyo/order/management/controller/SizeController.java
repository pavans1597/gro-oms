package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.groyyo.order.management.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.master.dto.request.SizeRequestDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("size")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/get/all")
    public ResponseDto<List<SizeResponseDto>> getAllSizes(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all sizes");

        List<SizeResponseDto> sizeResponseDtos = sizeService.getAllSizes(status);

        return ResponseDto.success("Found " + sizeResponseDtos.size() + " sizes in the system", sizeResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<SizeResponseDto> getSize(@PathVariable String id) {

        log.info("Request received to get size with id: {}", id);

        SizeResponseDto sizeResponseDto = sizeService.getSizeById(id);

        return Objects.isNull(sizeResponseDto) ? ResponseDto.failure("Found no size with id: " + id)
                : ResponseDto.success("Found size with id: " + id, sizeResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<SizeResponseDto> addSize(@RequestBody @Valid SizeRequestDto sizeRequestDto) {

        log.info("Request received to add size: {}", sizeRequestDto);

        SizeResponseDto sizeResponseDto = sizeService.addSize(sizeRequestDto);

        return Objects.isNull(sizeResponseDto) ? ResponseDto.failure("Unable to add size !!")
                : ResponseDto.success("Size added successfully !!", sizeResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<SizeResponseDto> updateSize(@RequestBody SizeRequestDto sizeRequestDto) {

        log.info("Request received to update size: {}", sizeRequestDto);

        SizeResponseDto sizeResponseDto = sizeService.updateSize(sizeRequestDto);

        return Objects.isNull(sizeResponseDto) ? ResponseDto.failure("Unable to update size !!")
                : ResponseDto.success("Size updated successfully !!", sizeResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<SizeResponseDto> activateDeactivateSize(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate size with id: {}", id);

        SizeResponseDto sizeResponseDto = sizeService.activateDeactivateSize(id, status);

        return Objects.isNull(sizeResponseDto) ? ResponseDto.failure("Found no size with id: " + id)
                : ResponseDto.success("Activated / Deactivated size with id: " + id, sizeResponseDto);
    }
}
