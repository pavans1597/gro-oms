package com.groyyo.order.service;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;


public interface CacheService {


	Pair<String, Map<String, ?>> getEntitiesFromCache(String entity);


	void populateEntitiesInCache(String entity);

}
