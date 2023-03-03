package com.groyyo.order.management.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.FabricRequestDto;
import com.groyyo.order.management.dto.response.FabricResponseDto;
import com.groyyo.order.management.service.FabricService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import javax.validation.Valid;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Log4j2
@RestController
@RequestMapping("fabric")
public class FabricController {

    @Autowired
    private FabricService fabricService;

    @GetMapping("get/all")
    public ResponseDto<List<FabricResponseDto>> getAllFabrics(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all fabrics");

        List<FabricResponseDto> fabricResponseDtos = fabricService.getAllFabrics(status);

        return ResponseDto.success("Found " + fabricResponseDtos.size() + " fabrics in the system", fabricResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<FabricResponseDto> getFabric(@PathVariable String id) {

        log.info("Request received to get fabric with id: {}", id);

        FabricResponseDto fabricResponseDto = fabricService.getFabricById(id);

        return Objects.isNull(fabricResponseDto) ? ResponseDto.failure("Found no fabric with id: " + id)
                : ResponseDto.success("Found fabric with id: " + id, fabricResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<FabricResponseDto> addFabric(@RequestBody @Valid FabricRequestDto fabricRequestDto) {

        log.info("Request received to add fabric: {}", fabricRequestDto);

        FabricResponseDto fabricResponseDto = fabricService.addFabric(fabricRequestDto);

        return Objects.isNull(fabricResponseDto) ? ResponseDto.failure("Unable to add fabric !!")
                : ResponseDto.success("Fabric added successfully !!", fabricResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<FabricResponseDto> updateFabric(@RequestBody FabricRequestDto fabricRequestDto) {

        log.info("Request received to update fabric: {}", fabricRequestDto);

        FabricResponseDto fabricResponseDto = fabricService.updateFabric(fabricRequestDto);

        return Objects.isNull(fabricResponseDto) ? ResponseDto.failure("Unable to update fabric !!")
                : ResponseDto.success("Fabric updated successfully !!", fabricResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<FabricResponseDto> activateDeactivateFabric(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate fabric with id: {}", id);

        FabricResponseDto fabricResponseDto = fabricService.activateDeactivateFabric(id, status);

        return Objects.isNull(fabricResponseDto) ? ResponseDto.failure("Found no fabric with id: " + id)
                : ResponseDto.success("Activated / Deactivated fabric with id: " + id, fabricResponseDto);
    }
}

