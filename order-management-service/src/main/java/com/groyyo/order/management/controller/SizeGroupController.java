package com.groyyo.order.management.controller;

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
import com.groyyo.core.master.dto.request.SizeGroupRequestDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.order.management.service.SizeGroupService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("sizeGroup")
public class SizeGroupController {

    @Autowired
    private SizeGroupService sizeGroupService;

    @GetMapping("get/all")
    public ResponseDto<List<SizeGroupResponseDto>> getAllSizeGroups(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all sizeGroups");

        List<SizeGroupResponseDto> sizeGroupResponseDtos = sizeGroupService.getAllSizeGroups(status);

        return ResponseDto.success("Found " + sizeGroupResponseDtos.size() + " sizeGroups in the system", sizeGroupResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<SizeGroupResponseDto> getSizeGroup(@PathVariable String id) {

        log.info("Request received to get sizeGroup with id: {}", id);

        SizeGroupResponseDto sizeGroupResponseDto = sizeGroupService.getSizeGroupById(id);

        return Objects.isNull(sizeGroupResponseDto) ? ResponseDto.failure("Found no sizeGroup with id: " + id)
                : ResponseDto.success("Found sizeGroup with id: " + id, sizeGroupResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<SizeGroupResponseDto> addSizeGroup(@RequestBody @Valid SizeGroupRequestDto sizeGroupRequestDto) {

        log.info("Request received to add sizeGroup: {}", sizeGroupRequestDto);

        SizeGroupResponseDto sizeGroupResponseDto = sizeGroupService.addSizeGroup(sizeGroupRequestDto);

        return Objects.isNull(sizeGroupResponseDto) ? ResponseDto.failure("Unable to add sizeGroup !!")
                : ResponseDto.success("SizeGroup added successfully !!", sizeGroupResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<SizeGroupResponseDto> updateSizeGroup(@RequestBody SizeGroupRequestDto sizeGroupRequestDto) {

        log.info("Request received to update sizeGroup: {}", sizeGroupRequestDto);

        SizeGroupResponseDto sizeGroupResponseDto = sizeGroupService.updateSizeGroup(sizeGroupRequestDto);

        return Objects.isNull(sizeGroupResponseDto) ? ResponseDto.failure("Unable to update sizeGroup !!")
                : ResponseDto.success("SizeGroup updated successfully !!", sizeGroupResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<SizeGroupResponseDto> activateDeactivateSizeGroup(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate sizeGroup with id: {}", id);

        SizeGroupResponseDto sizeGroupResponseDto = sizeGroupService.activateDeactivateSizeGroup(id, status);

        return Objects.isNull(sizeGroupResponseDto) ? ResponseDto.failure("Found no sizeGroup with id: " + id)
                : ResponseDto.success("Activated / Deactivated sizeGroup with id: " + id, sizeGroupResponseDto);
    }
}
