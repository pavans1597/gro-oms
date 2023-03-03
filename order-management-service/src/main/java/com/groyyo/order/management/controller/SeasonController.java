package com.groyyo.order.management.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.groyyo.order.management.service.SeasonService;
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
import com.groyyo.core.master.dto.request.SeasonRequestDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("season")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    @GetMapping("get/all")
    public ResponseDto<List<SeasonResponseDto>> getAllSeasons(
            @RequestParam(value = "status", required = false) Boolean status) {

        log.info("Request received to get all seasons");

        List<SeasonResponseDto> seasonResponseDtos = seasonService.getAllSeasons(status);

        return ResponseDto.success("Found " + seasonResponseDtos.size() + " seasons in the system", seasonResponseDtos);
    }

    @GetMapping("get/{id}")
    public ResponseDto<SeasonResponseDto> getSeason(@PathVariable String id) {

        log.info("Request received to get season with id: {}", id);

        SeasonResponseDto seasonResponseDto = seasonService.getSeasonById(id);

        return Objects.isNull(seasonResponseDto) ? ResponseDto.failure("Found no season with id: " + id)
                : ResponseDto.success("Found season with id: " + id, seasonResponseDto);
    }

    @PostMapping("add")
    public ResponseDto<SeasonResponseDto> addSeason(@RequestBody @Valid SeasonRequestDto seasonRequestDto) {

        log.info("Request received to add season: {}", seasonRequestDto);

        SeasonResponseDto seasonResponseDto = seasonService.addSeason(seasonRequestDto);

        return Objects.isNull(seasonResponseDto) ? ResponseDto.failure("Unable to add season !!")
                : ResponseDto.success("Season added successfully !!", seasonResponseDto);
    }

    @PostMapping("update")
    public ResponseDto<SeasonResponseDto> updateSeason(@RequestBody SeasonRequestDto seasonRequestDto) {

        log.info("Request received to update season: {}", seasonRequestDto);

        SeasonResponseDto seasonResponseDto = seasonService.updateSeason(seasonRequestDto);

        return Objects.isNull(seasonResponseDto) ? ResponseDto.failure("Unable to update season !!")
                : ResponseDto.success("Season updated successfully !!", seasonResponseDto);
    }

    @PutMapping("{id}/toggle/status/{status}")
    public ResponseDto<SeasonResponseDto> activateDeactivateSeason(@PathVariable String id, @PathVariable boolean status) {

        log.info("Request received to activate / deactivate season with id: {}", id);

        SeasonResponseDto seasonResponseDto = seasonService.activateDeactivateSeason(id, status);

        return Objects.isNull(seasonResponseDto) ? ResponseDto.failure("Found no season with id: " + id)
                : ResponseDto.success("Activated / Deactivated season with id: " + id, seasonResponseDto);
    }
}

