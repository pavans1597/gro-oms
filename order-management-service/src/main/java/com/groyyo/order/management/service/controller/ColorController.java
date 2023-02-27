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
import com.groyyo.core.master.dto.request.ColorRequestDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;
import com.groyyo.order.management.service.service.ColorService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("color")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping("get/all")
    public ResponseDto<List<ColorResponseDto>> getAllColors(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all colors");

        List<ColorResponseDto> colorResponseDtos = colorService.getAllColors(status);

        return ResponseDto.success("Found " + colorResponseDtos.size() + " colors in the system", colorResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<ColorResponseDto> getColor(@PathVariable String id) {

        log.info("Request received to get color with id: {}", id);

        ColorResponseDto colorResponseDto = colorService.getColorById(id);

        return Objects.isNull(colorResponseDto) ? ResponseDto.failure("Found no color with id: " + id)
                : ResponseDto.success("Found color with id: " + id, colorResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<ColorResponseDto> addColor(@RequestBody @Valid ColorRequestDto colorRequestDto) {

        log.info("Request received to add color: {}", colorRequestDto);

        ColorResponseDto colorResponseDto = colorService.addColor(colorRequestDto);

        return Objects.isNull(colorResponseDto) ? ResponseDto.failure("Unable to add color !!")
                : ResponseDto.success("Color added successfully !!", colorResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<ColorResponseDto> updateColor(@RequestBody ColorRequestDto colorRequestDto) {

        log.info("Request received to update color: {}", colorRequestDto);

        ColorResponseDto colorResponseDto = colorService.updateColor(colorRequestDto);

        return Objects.isNull(colorResponseDto) ? ResponseDto.failure("Unable to update color !!")
                : ResponseDto.success("Color updated successfully !!", colorResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<ColorResponseDto> activateDeactivateColor(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate color with id: {}", id);

        ColorResponseDto colorResponseDto = colorService.activateDeactivateColor(id, status);

        return Objects.isNull(colorResponseDto) ? ResponseDto.failure("Found no color with id: " + id)
                : ResponseDto.success("Activated / Deactivated color with id: " + id, colorResponseDto);
    }
}
