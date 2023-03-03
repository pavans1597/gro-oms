package com.groyyo.order.management.controller;

import com.groyyo.order.management.constants.CacheConstants;
import com.groyyo.order.management.service.CacheService;
import com.groyyo.core.base.common.dto.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("master/{entity}/get/all")
    public ResponseDto<Map<String, ?>> getLocationDataFromCache(@PathVariable(name = CacheConstants.ENTITY, value = CacheConstants.ENTITY) String entity) {

        log.info("Got request to get {} entities from cache ", entity);

        Pair<String, Map<String, ?>> resultPair = cacheService.getEntitiesFromCache(entity);

        return ResponseDto.success(resultPair.getLeft(), resultPair.getRight());
    }

    @PostMapping("master/{entity}/populate/all")
    public ResponseDto<Void> populateLocationDataInCache(@PathVariable(name = CacheConstants.ENTITY, value = CacheConstants.ENTITY) String entity) {

        log.info("Got request to populate {} entities in cache ", entity);

        cacheService.populateEntitiesInCache(entity);

        return ResponseDto.success("Done");
    }

}