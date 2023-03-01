package com.groyyo.order.controller;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.dto.request.FabricCategoryRequestDto;
import com.groyyo.order.dto.response.FabricCategoryResponseDto;
import com.groyyo.order.service.FabricCategoryService;
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
@RequestMapping("fabricCategory")
public class FabricCategoryController {

    @Autowired
    private FabricCategoryService fabricCategoryService;

    @GetMapping("get/all")
    public ResponseDto<List<FabricCategoryResponseDto>> getAllFabricCategorys(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all fabricCategorys");

        List<FabricCategoryResponseDto> fabricCategoryResponseDtos = fabricCategoryService.getAllFabricCategorys(status);

        return ResponseDto.success("Found " + fabricCategoryResponseDtos.size() + " fabricCategorys in the system", fabricCategoryResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<FabricCategoryResponseDto> getFabricCategory(@PathVariable String id) {

        log.info("Request received to get fabricCategory with id: {}", id);

        FabricCategoryResponseDto fabricCategoryResponseDto = fabricCategoryService.getFabricCategoryById(id);

        return Objects.isNull(fabricCategoryResponseDto) ? ResponseDto.failure("Found no fabricCategory with id: " + id)
                : ResponseDto.success("Found fabricCategory with id: " + id, fabricCategoryResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<FabricCategoryResponseDto> addFabricCategory(@RequestBody @Valid FabricCategoryRequestDto fabricCategoryRequestDto) {

        log.info("Request received to add fabricCategory: {}", fabricCategoryRequestDto);

        FabricCategoryResponseDto fabricCategoryResponseDto = fabricCategoryService.addFabricCategory(fabricCategoryRequestDto);

        return Objects.isNull(fabricCategoryResponseDto) ? ResponseDto.failure("Unable to add fabricCategory !!")
                : ResponseDto.success("FabricCategory added successfully !!", fabricCategoryResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<FabricCategoryResponseDto> updateFabricCategory(@RequestBody FabricCategoryRequestDto fabricCategoryRequestDto) {

        log.info("Request received to update fabricCategory: {}", fabricCategoryRequestDto);

        FabricCategoryResponseDto fabricCategoryResponseDto = fabricCategoryService.updateFabricCategory(fabricCategoryRequestDto);

        return Objects.isNull(fabricCategoryResponseDto) ? ResponseDto.failure("Unable to update fabricCategory !!")
                : ResponseDto.success("FabricCategory updated successfully !!", fabricCategoryResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<FabricCategoryResponseDto> activateDeactivateFabricCategory(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate fabricCategory with id: {}", id);

        FabricCategoryResponseDto fabricCategoryResponseDto = fabricCategoryService.activateDeactivateFabricCategory(id, status);

        return Objects.isNull(fabricCategoryResponseDto) ? ResponseDto.failure("Found no fabricCategory with id: " + id)
                : ResponseDto.success("Activated / Deactivated fabricCategory with id: " + id, fabricCategoryResponseDto);
    }
}

