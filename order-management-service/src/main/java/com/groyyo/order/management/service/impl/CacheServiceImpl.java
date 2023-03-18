package com.groyyo.order.management.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.groyyo.order.management.cache.MasterDataLocalCache;
import com.groyyo.order.management.constants.CacheConstants;
import com.groyyo.order.management.service.CacheService;
import com.groyyo.order.management.service.ColorService;
import com.groyyo.order.management.service.FitService;
import com.groyyo.order.management.service.PartService;
import com.groyyo.order.management.service.ProductService;
import com.groyyo.order.management.service.SeasonService;
import com.groyyo.order.management.service.SizeGroupService;
import com.groyyo.order.management.service.SizeService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CacheServiceImpl implements CacheService {

	private MasterDataLocalCache masterDataLocalCache;

	public void setMasterDataLocalCache(MasterDataLocalCache masterDataLocalCache) {
		this.masterDataLocalCache = masterDataLocalCache;
	}

	public MasterDataLocalCache getMasterDataLocalCache() {
		return masterDataLocalCache;
	}

	@Autowired
	private ColorService colorService;

	@Autowired
	private FitService fitService;

	@Autowired
	private PartService partService;

	@Autowired
	private ProductService productService;

	@Autowired
	private SeasonService seasonService;

	@Autowired
	private SizeGroupService sizeGroupService;

	@Autowired
	private SizeService sizeService;

	@Override
	public Pair<String, Map<String, ?>> getEntitiesFromCache(String entity) {
		log.info("Going to get entity: {} from cache ", entity);

		Map<String, ?> results = new HashMap<>();

		switch (entity) {

		case CacheConstants.COLOR:
			results = masterDataLocalCache.getColorByNameMap();
			break;

		case CacheConstants.DEFECT:
			results = masterDataLocalCache.getDefectByNameMap();
			break;

		case CacheConstants.FIT:
			results = masterDataLocalCache.getFitByNameMap();
			break;

		case CacheConstants.PART:
			results = masterDataLocalCache.getPartByNameMap();
			break;

		case CacheConstants.PRODUCT:
			results = masterDataLocalCache.getProductByNameMap();
			break;

		case CacheConstants.PRODUCT_DEFECT:
			results = masterDataLocalCache.getProductDefectByNameMap();
			break;

		case CacheConstants.SEASON:
			results = masterDataLocalCache.getSeasonByNameMap();
			break;

		case CacheConstants.SIZE:
			results = masterDataLocalCache.getSizeByNameMap();
			break;

		case CacheConstants.SIZE_GROUP:
			results = masterDataLocalCache.getSizeGroupByNameMap();
			break;

		default:
			String responseMsg = "No valid entity values to look for in cache. Allowed values are: " + CacheConstants.ENTITY_LIST_GET;
			log.error(responseMsg);
			return Pair.of(responseMsg, results);
		}

		return Pair.of("Fetched " + results.size() + " " + entity + " from cache", results);
	}

	@Override
	public void populateEntitiesInCache(String entity) {
		log.info("Going to populate entity: {} in cache ", entity);

		switch (entity) {

		case CacheConstants.ALL:
			masterDataLocalCache.refreshCache();
			break;

		case CacheConstants.COLOR:
			masterDataLocalCache.populateAllColors();
			break;

		case CacheConstants.DEFECT:
			masterDataLocalCache.populateAllDefects();
			break;

		case CacheConstants.FIT:
			masterDataLocalCache.populateAllFits();
			break;

		case CacheConstants.PART:
			masterDataLocalCache.populateAllParts();
			break;

		case CacheConstants.PRODUCT:
			masterDataLocalCache.populateAllProducts();
			break;

		case CacheConstants.PRODUCT_DEFECT:
			masterDataLocalCache.populateAllProductDefects();
			break;

		case CacheConstants.SEASON:
			masterDataLocalCache.populateAllSeasons();
			break;

		case CacheConstants.SIZE:
			masterDataLocalCache.populateAllSizes();
			break;

		case CacheConstants.SIZE_GROUP:
			masterDataLocalCache.populateAllSizeGroups();
			break;

		default:
			log.error("No valid entity values to populate in cache. Allowed values are: ", CacheConstants.ENTITY_LIST_POPULATE);
			break;
		}
	}

	@Override
	@Async("orderThreadExecutor")
	public void saveEntitiesFromCache(String entity) {
		log.info("Going to save entity: {} from cache ", entity);

		switch (entity) {

		case CacheConstants.ALL:
			saveAllEntitiesFromCache();
			break;

		case CacheConstants.COLOR:
			colorService.saveEntityFromCache(masterDataLocalCache.getColorByNameMap());
			break;

		case CacheConstants.DEFECT:
			// TODO implementation to be added
			break;

		case CacheConstants.FIT:
			fitService.saveEntityFromCache(masterDataLocalCache.getFitByNameMap());
			break;

		case CacheConstants.PART:
			partService.saveEntityFromCache(masterDataLocalCache.getPartByNameMap());
			break;

		case CacheConstants.PRODUCT:
			productService.saveEntityFromCache(masterDataLocalCache.getProductByNameMap());
			break;

		case CacheConstants.PRODUCT_DEFECT:
			// TODO implementation to be added
			break;

		case CacheConstants.SEASON:
			seasonService.saveEntityFromCache(masterDataLocalCache.getSeasonByNameMap());
			break;

		case CacheConstants.SIZE:
			sizeService.saveEntityFromCache(masterDataLocalCache.getSizeByNameMap());
			break;

		case CacheConstants.SIZE_GROUP:
			sizeGroupService.saveEntityFromCache(masterDataLocalCache.getSizeGroupByNameMap());
			break;

		default:
			log.error("No valid entity values to save from cache. Allowed values are: ", CacheConstants.ENTITY_LIST_POPULATE);
			break;
		}
	}

	@Override
	public void saveAllEntitiesFromCache() {

		log.info("Going to save all cache entities in local db");

		CacheConstants.ENTITY_LIST_SAVE.forEach(entity -> {

			log.info("Running iteration for entity: {}", entity);

			CompletableFuture.runAsync(() -> {

				saveEntitiesFromCache(entity);
			});
		});

	}
}
