package com.groyyo.order.controller;


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
import com.groyyo.core.master.dto.request.PartRequestDto;
import com.groyyo.core.master.dto.response.PartResponseDto;
import com.groyyo.order.service.PartService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("part")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping("get/all")
    public ResponseDto<List<PartResponseDto>> getAllParts(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all parts");

        List<PartResponseDto> partResponseDtos = partService.getAllParts(status);

        return ResponseDto.success("Found " + partResponseDtos.size() + " parts in the system", partResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<PartResponseDto> getPart(@PathVariable String id) {

        log.info("Request received to get part with id: {}", id);

        PartResponseDto partResponseDto = partService.getPartById(id);

        return Objects.isNull(partResponseDto) ? ResponseDto.failure("Found no part with id: " + id)
                : ResponseDto.success("Found part with id: " + id, partResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<PartResponseDto> addPart(@RequestBody @Valid PartRequestDto partRequestDto) {

        log.info("Request received to add part: {}", partRequestDto);

        PartResponseDto partResponseDto = partService.addPart(partRequestDto);

        return Objects.isNull(partResponseDto) ? ResponseDto.failure("Unable to add part !!")
                : ResponseDto.success("Part added successfully !!", partResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<PartResponseDto> updatePart(@RequestBody PartRequestDto partRequestDto) {

        log.info("Request received to update part: {}", partRequestDto);

        PartResponseDto partResponseDto = partService.updatePart(partRequestDto);

        return Objects.isNull(partResponseDto) ? ResponseDto.failure("Unable to update part !!")
                : ResponseDto.success("Part updated successfully !!", partResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<PartResponseDto> activateDeactivatePart(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate part with id: {}", id);

        PartResponseDto partResponseDto = partService.activateDeactivatePart(id, status);

        return Objects.isNull(partResponseDto) ? ResponseDto.failure("Found no part with id: " + id)
                : ResponseDto.success("Activated / Deactivated part with id: " + id, partResponseDto);
    }
}
