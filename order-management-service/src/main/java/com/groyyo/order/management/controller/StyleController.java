package com.groyyo.order.management.controller;
import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.dto.response.StyleDto;
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
    public ResponseDto<List<StyleDto>> getAllStyles(@RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all styles");

        List<StyleDto> styleDtos = styleService.getAllStyles(status);

        return ResponseDto.success("Found " + styleDtos.size() + " styles in the system", styleDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<StyleDto> getStyle(@PathVariable String id) {

        log.info("Request received to get style with id: {}", id);

        StyleDto styleDto = styleService.getStyleById(id);

        return Objects.isNull(styleDto) ? ResponseDto.failure("Found no style with id: " + id) : ResponseDto.success("Found style with id: " + id, styleDto);
    }

    @PostMapping("add")
    public ResponseDto<StyleDto> addStyle(@RequestBody @Valid StyleDto styleRequestDto) {

        log.info("Request received to add style: {}", styleRequestDto);

        StyleDto styleDto = styleService.addStyle(styleRequestDto);

        return Objects.isNull(styleDto) ? ResponseDto.failure("Unable to add style !!") : ResponseDto.success("Style added successfully !!", styleDto);
    }

    @PostMapping("update")
    public ResponseDto<StyleDto> updateStyle(@RequestBody StyleDto styleUpdateDto) {

        log.info("Request received to update style: {}", styleUpdateDto);

        StyleDto styleDto = styleService.updateStyle(styleUpdateDto);

        return Objects.isNull(styleDto) ? ResponseDto.failure("Unable to update style !!") : ResponseDto.success("Style updated successfully !!", styleDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<StyleDto> activateDeactivateStyle(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate style with id: {}", id);

        StyleDto styleDto = styleService.activateDeactivateStyle(id, status);

        return Objects.isNull(styleDto) ? ResponseDto.failure("Found no style with id: " + id) : ResponseDto.success("Activated / Deactivated style with id: " + id, styleDto);
    }
}
