package com.groyyo.order.management.service.service.impl;

import com.groyyo.order.management.service.cache.MasterDataLocalCache;
import com.groyyo.order.management.service.constants.CacheConstants;
import com.groyyo.order.management.service.service.CacheService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Log4j2
@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private MasterDataLocalCache masterDataLocalCache;

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

}
