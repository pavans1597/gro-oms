package com.groyyo.order.management.controller;
import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.request.StyleRequestDto;
import com.groyyo.order.management.dto.request.StyleUpdateDto;
import com.groyyo.order.management.dto.response.StyleResponseDto;
import com.groyyo.order.management.service.StyleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("style")
public class StyleController {

    @Autowired
    private StyleService styleService;

    @GetMapping("get/all")
    public ResponseDto<List<StyleResponseDto>> getAllStyles(@RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all styles");

        List<StyleResponseDto> styleResponseDtos = styleService.getAllStyles(status);

        return ResponseDto.success("Found " + styleResponseDtos.size() + " styles in the system", styleResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<StyleResponseDto> getStyle(@PathVariable String id) {

        log.info("Request received to get style with id: {}", id);

        StyleResponseDto styleResponseDto = styleService.getStyleById(id);

        return Objects.isNull(styleResponseDto) ? ResponseDto.failure("Found no style with id: " + id) : ResponseDto.success("Found style with id: " + id, styleResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<StyleResponseDto> addStyle(@RequestBody @Valid StyleRequestDto styleRequestDto) {

        log.info("Request received to add style: {}", styleRequestDto);

        StyleResponseDto styleResponseDto = styleService.addStyle(styleRequestDto);

        return Objects.isNull(styleResponseDto) ? ResponseDto.failure("Unable to add style !!") : ResponseDto.success("Style added successfully !!", styleResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<StyleResponseDto> updateStyle(@RequestBody StyleUpdateDto styleUpdateDto) {

        log.info("Request received to update style: {}", styleUpdateDto);

        StyleResponseDto styleResponseDto = styleService.updateStyle(styleUpdateDto);

        return Objects.isNull(styleResponseDto) ? ResponseDto.failure("Unable to update style !!") : ResponseDto.success("Style updated successfully !!", styleResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<StyleResponseDto> activateDeactivateStyle(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate style with id: {}", id);

        StyleResponseDto styleResponseDto = styleService.activateDeactivateStyle(id, status);

        return Objects.isNull(styleResponseDto) ? ResponseDto.failure("Found no style with id: " + id) : ResponseDto.success("Activated / Deactivated style with id: " + id, styleResponseDto);
    }
}
