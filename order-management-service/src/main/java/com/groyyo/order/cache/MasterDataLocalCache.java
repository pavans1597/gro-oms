package com.groyyo.order.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.groyyo.core.base.http.GroyyoRestClient;
import com.groyyo.core.master.dto.response.BaseResponseDto;
import com.groyyo.core.master.dto.response.ColorResponseDto;
import com.groyyo.core.master.dto.response.DefectResponseDto;
import com.groyyo.core.master.dto.response.FitResponseDto;
import com.groyyo.core.master.dto.response.PartResponseDto;
import com.groyyo.core.master.dto.response.ProductDefectResponseDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.core.master.dto.response.SeasonResponseDto;
import com.groyyo.core.master.dto.response.SizeGroupResponseDto;
import com.groyyo.core.master.dto.response.SizeResponseDto;
import com.groyyo.core.masterData.client.api.MasterDataApi;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MasterDataLocalCache {

	@Autowired
	@Qualifier("masterDataClient")
	private GroyyoRestClient masterDataClient;

	@Value("${cache.masterData.enable:true}")
	private boolean cacheMasterDataEnable;

	@Autowired
	private MasterDataApi masterDataApi;

	@PostConstruct
	public void init() {

		if (cacheMasterDataEnable)
			refreshCache();
		else
			log.info("Not hitting the master-data-service to populate master data as cache is disabled");
	}

	private Map<String, ColorResponseDto> colorByNameMap = new HashMap<String, ColorResponseDto>();
	private Map<String, DefectResponseDto> defectByNameMap = new HashMap<String, DefectResponseDto>();
	private Map<String, FitResponseDto> fitByNameMap = new HashMap<String, FitResponseDto>();
	private Map<String, PartResponseDto> partByNameMap = new HashMap<String, PartResponseDto>();
	private Map<String, ProductResponseDto> productByNameMap = new HashMap<String, ProductResponseDto>();
	private Map<String, ProductDefectResponseDto> productDefectByNameMap = new HashMap<String, ProductDefectResponseDto>();
	private Map<String, SeasonResponseDto> seasonByNameMap = new HashMap<String, SeasonResponseDto>();
	private Map<String, SizeResponseDto> sizeByNameMap = new HashMap<String, SizeResponseDto>();
	private Map<String, SizeGroupResponseDto> sizeGroupByNameMap = new HashMap<String, SizeGroupResponseDto>();

	/**
	 * Sample code to include in any service to fetch and populate master data
	 */
	public void refreshCache() {

		populateAllColors();
		populateAllDefects();
		populateAllFits();
		populateAllParts();
		populateAllProducts();
		populateAllProductDefects();
		populateAllSeasons();
		populateAllSizes();
		populateAllSizeGroups();
	}

	/**
	 * Function to fetch colors data from master-data-service and populate
	 * colorByNameMap
	 */
	public void populateAllColors() {

		try {

			colorByNameMap = new PopulateMap<ColorResponseDto>().populateMap(fetchAllColors(), colorByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all colors in cache: ", e);
		}
	}

	/**
	 * Function to fetch defects data from master-data-service and populate
	 * defectByNameMap
	 */
	public void populateAllDefects() {

		try {

			defectByNameMap = new PopulateMap<DefectResponseDto>().populateMap(fetchAllDefects(), defectByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all defects in cache: ", e);
		}

	}

	/**
	 * Function to fetch fits data from master-data-service and populate
	 * fitByNameMap
	 */
	public void populateAllFits() {

		try {

			fitByNameMap = new PopulateMap<FitResponseDto>().populateMap(fetchAllFits(), fitByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all fits in cache: ", e);
		}

	}

	/**
	 * Function to fetch parts data from master-data-service and populate
	 * partByNameMap
	 */
	public void populateAllParts() {

		try {

			partByNameMap = new PopulateMap<PartResponseDto>().populateMap(fetchAllParts(), partByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all parts in cache: ", e);
		}

	}

	/**
	 * Function to fetch products data from master-data-service and populate
	 * productByNameMap
	 */
	public void populateAllProducts() {

		try {

			productByNameMap = new PopulateMap<ProductResponseDto>().populateMap(fetchAllProducts(), productByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all products in cache: ", e);
		}

	}

	/**
	 * Function to fetch product defects data from master-data-service and populate
	 * productDefectByNameMap
	 */
	public void populateAllProductDefects() {

		try {

			productDefectByNameMap = new PopulateMap<ProductDefectResponseDto>().populateMap(fetchAllProductDefects(), productDefectByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all productDefects in cache: ", e);
		}

	}

	/**
	 * Function to fetch seasons data from master-data-service and populate
	 * seasonByNameMap
	 */
	public void populateAllSeasons() {

		try {

			seasonByNameMap = new PopulateMap<SeasonResponseDto>().populateMap(fetchAllSeasons(), seasonByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all seasons in cache: ", e);
		}

	}

	/**
	 * Function to fetch sizes data from master-data-service and populate
	 * sizeByNameMap
	 */
	public void populateAllSizes() {

		try {

			sizeByNameMap = new PopulateMap<SizeResponseDto>().populateMap(fetchAllSizes(), sizeByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all sizes in cache: ", e);
		}

	}

	/**
	 * Function to fetch size groups data from master-data-service and populate
	 * sizeGroupByNameMap
	 */
	public void populateAllSizeGroups() {

		try {

			sizeGroupByNameMap = new PopulateMap<SizeGroupResponseDto>().populateMap(fetchAllSizeGroups(), sizeGroupByNameMap);

		} catch (Exception e) {

			log.error("Failed to load all sizeGroups in cache: ", e);
		}

	}

	private List<ColorResponseDto> fetchAllColors() {

		List<ColorResponseDto> colors = new ArrayList<ColorResponseDto>();

		try {

			colors = masterDataApi.getAllColors().getData();
			// masterDataCache.getAllColors();

			log.info("colors: {}", colors);

		} catch (Exception e) {

			log.error("Failed to fetch all colors from master data service: ", e);
		}

		return colors;
	}

	private List<DefectResponseDto> fetchAllDefects() {

		List<DefectResponseDto> defects = new ArrayList<DefectResponseDto>();

		try {

			defects = masterDataApi.getAllDefects().getData();
			// masterDataCache.getAllDefects();

			log.info("defects: {}", defects);

		} catch (Exception e) {

			log.error("Failed to fetch all defects from master data service: ", e);
		}

		return defects;
	}

	private List<FitResponseDto> fetchAllFits() {

		List<FitResponseDto> fits = new ArrayList<FitResponseDto>();

		try {

			fits = masterDataApi.getAllFits().getData();
			// masterDataCache.getAllFits();

			log.info("fits: {}", fits);

		} catch (Exception e) {

			log.error("Failed to fetch all fits from master data service: ", e);
		}

		return fits;
	}

	private List<PartResponseDto> fetchAllParts() {

		List<PartResponseDto> parts = new ArrayList<PartResponseDto>();

		try {

			parts = masterDataApi.getAllParts().getData();
			// masterDataCache.getAllParts();

			log.info("parts: {}", parts);

		} catch (Exception e) {

			log.error("Failed to fetch all parts from master data service: ", e);
		}

		return parts;
	}

	private List<ProductResponseDto> fetchAllProducts() {

		List<ProductResponseDto> products = new ArrayList<ProductResponseDto>();

		try {

			products = masterDataApi.getAllProducts().getData();
			// masterDataCache.getAllProducts();

			log.info("products: {}", products);

		} catch (Exception e) {

			log.error("Failed to fetch all products from master data service: ", e);
		}

		return products;
	}

	private List<ProductDefectResponseDto> fetchAllProductDefects() {

		List<ProductDefectResponseDto> productDefects = new ArrayList<ProductDefectResponseDto>();

		try {

			productDefects = masterDataApi.getAllProductDefects().getData();
			// masterDataCache.getAllProductDefects();

			log.info("productDefects: {}", productDefects);

		} catch (Exception e) {

			log.error("Failed to fetch all productDefects from master data service: ", e);
		}

		return productDefects;
	}

	private List<SeasonResponseDto> fetchAllSeasons() {

		List<SeasonResponseDto> seasons = new ArrayList<SeasonResponseDto>();

		try {

			seasons = masterDataApi.getAllSeasons().getData();
			// masterDataCache.getAllSeasons();

			log.info("seasons: {}", seasons);

		} catch (Exception e) {

			log.error("Failed to fetch all seasons from master data service: ", e);
		}

		return seasons;
	}

	private List<SizeResponseDto> fetchAllSizes() {

		List<SizeResponseDto> sizes = new ArrayList<SizeResponseDto>();

		try {

			sizes = masterDataApi.getAllSizes().getData();
			// masterDataCache.getAllSizes();

			log.info("sizes: {}", sizes);

		} catch (Exception e) {

			log.error("Failed to fetch all sizes from master data service: ", e);
		}

		return sizes;
	}

	private List<SizeGroupResponseDto> fetchAllSizeGroups() {

		List<SizeGroupResponseDto> sizeGroups = new ArrayList<SizeGroupResponseDto>();

		try {

			sizeGroups = masterDataApi.getAllSizeGroups().getData();
			// masterDataCache.getAllSizeGroups();

			log.info("sizeGroups: {}", sizeGroups);

		} catch (Exception e) {

			log.error("Failed to fetch all sizeGroups from master data service: ", e);
		}

		return sizeGroups;
	}

	public Map<String, ColorResponseDto> getColorByNameMap() {

		return colorByNameMap;
	}

	public Map<String, DefectResponseDto> getDefectByNameMap() {

		return defectByNameMap;
	}

	public Map<String, FitResponseDto> getFitByNameMap() {

		return fitByNameMap;
	}

	public Map<String, PartResponseDto> getPartByNameMap() {

		return partByNameMap;
	}

	public Map<String, ProductResponseDto> getProductByNameMap() {

		return productByNameMap;
	}

	public Map<String, ProductDefectResponseDto> getProductDefectByNameMap() {

		return productDefectByNameMap;
	}

	public Map<String, SeasonResponseDto> getSeasonByNameMap() {

		return seasonByNameMap;
	}

	public Map<String, SizeResponseDto> getSizeByNameMap() {

		return sizeByNameMap;
	}

	public Map<String, SizeGroupResponseDto> getSizeGroupByNameMap() {

		return sizeGroupByNameMap;
	}

	class PopulateMap<T extends BaseResponseDto> {

		private Map<String, T> populateMap(List<T> baseResponseDtos, Map<String, T> map) {

			return baseResponseDtos.stream().collect(Collectors.toMap(BaseResponseDto::getName, Function.identity()));
		}
	}
}
