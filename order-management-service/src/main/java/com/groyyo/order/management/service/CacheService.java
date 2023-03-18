package com.groyyo.order.management.service;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.groyyo.order.management.cache.MasterDataLocalCache;

public interface CacheService {

	Pair<String, Map<String, ?>> getEntitiesFromCache(String entity);

	void populateEntitiesInCache(String entity);

	/**
	 * @param entity
	 */
	void saveEntitiesFromCache(String entity);

	/**
	 * 
	 */
	void saveAllEntitiesFromCache();

	/**
	 * @param masterDataLocalCache
	 */
	void setMasterDataLocalCache(MasterDataLocalCache masterDataLocalCache);

}
