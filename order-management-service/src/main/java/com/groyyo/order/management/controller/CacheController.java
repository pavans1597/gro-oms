package com.groyyo.order.management.controller;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.order.management.constants.CacheConstants;
import com.groyyo.order.management.service.CacheService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("cache")
public class CacheController {

	@Autowired
	private CacheService cacheService;

	@GetMapping("master/{entity}/get/all")
	public ResponseDto<Map<String, ?>> getMasterDataFromCache(@PathVariable(name = CacheConstants.ENTITY, value = CacheConstants.ENTITY) String entity) {

		log.info("Got request to get {} entities from cache ", entity);

		Pair<String, Map<String, ?>> resultPair = cacheService.getEntitiesFromCache(entity);

		return ResponseDto.success(resultPair.getLeft(), resultPair.getRight());
	}

	@PostMapping("master/{entity}/populate/all")
	public ResponseDto<Void> populateMasterDataInCache(@PathVariable(name = CacheConstants.ENTITY, value = CacheConstants.ENTITY) String entity) {

		log.info("Got request to populate {} entities in cache ", entity);

		cacheService.populateEntitiesInCache(entity);

		return ResponseDto.success("Done");
	}

	@PostMapping("master/{entity}/save/all")
	public ResponseDto<Void> saveMasterDataInCache(@PathVariable(name = CacheConstants.ENTITY, value = CacheConstants.ENTITY) String entity) {

		log.info("Got request to save {} entities from cache ", entity);

		cacheService.saveEntitiesFromCache(entity);

		return ResponseDto.success("Done");
	}

}