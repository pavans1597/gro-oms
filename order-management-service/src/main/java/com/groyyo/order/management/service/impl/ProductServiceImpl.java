package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.core.master.dto.request.ProductRequestDto;
import com.groyyo.core.master.dto.response.ProductResponseDto;
import com.groyyo.order.management.adapter.ProductAdapter;
import com.groyyo.order.management.db.service.ProductDbService;
import com.groyyo.order.management.entity.Product;
import com.groyyo.order.management.http.service.FactoryHttpService;
import com.groyyo.order.management.service.ProductService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

	@Value("${kafka.master.updates.topic}")
	private String kafkaMasterDataUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	private ProductDbService productDbService;

	@Autowired
	private FactoryHttpService factoryHttpService;

	@Override
	public List<ProductResponseDto> getAllProducts(Boolean status) {

		log.info("Serving request to get all products");
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<Product> productEntities = Objects.isNull(status) ? productDbService.getAllProducts(factoryId)
				: productDbService.getAllProductsForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(productEntities)) {
			log.error("No Products found in the system");
			return new ArrayList<ProductResponseDto>();
		}

		return ProductAdapter.buildResponsesFromEntities(productEntities);
	}

	@Override
	public ProductResponseDto getProductById(String id) {

		log.info("Serving request to get a product by id:{}", id);

		Product product = productDbService.getProductById(id);

		if (Objects.isNull(product)) {
			String errorMsg = "Product with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return ProductAdapter.buildResponseFromEntity(product);
	}

	@Override
	public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {

		log.info("Serving request to add a product with request object:{}", productRequestDto);

		runValidations(productRequestDto);
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		Product product = ProductAdapter.buildProductFromRequest(productRequestDto, factoryId);

		product = productDbService.saveProduct(product);

		if (Objects.isNull(product)) {
			log.error("Unable to add product for object: {}", productRequestDto);
			return null;
		}

		// publishProduct(productResponseDto, KafkaConstants.KAFKA_PRODUCT_TYPE,
		// KafkaConstants.KAFKA_PRODUCT_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

		return ProductAdapter.buildResponseFromEntity(product);
	}

	@Override
	public ProductResponseDto updateProduct(ProductRequestDto productRequestDto) {

		log.info("Serving request to update a product with request object:{}", productRequestDto);

		Product product = productDbService.getProductById(productRequestDto.getId());

		if (Objects.isNull(product)) {
			log.error("Product with id: {} not found in the system", productRequestDto.getId());
			return null;
		}

		runValidations(productRequestDto);

		product = ProductAdapter.cloneProductWithRequest(productRequestDto, product);

		productDbService.saveProduct(product);

		// publishProduct(productResponseDto, KafkaConstants.KAFKA_PRODUCT_TYPE,
		// KafkaConstants.KAFKA_PRODUCT_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

		return ProductAdapter.buildResponseFromEntity(product);
	}

	@Override
	public ProductResponseDto conditionalSaveProduct(ProductResponseDto productResponseDto) {

		log.info("Serving request to conditionally save a Product with response object: {}", productResponseDto);

		Product product = productDbService.findByNameAndFactoryId(productResponseDto.getName(), productResponseDto.getFactoryId());

		if (Objects.nonNull(product)) {
			log.error("Product already exists with name: {} and factory_id: {}", productResponseDto.getName(), productResponseDto.getFactoryId());
			return null;
		}

		product = ProductAdapter.buildProductFromResponse(productResponseDto, productResponseDto.getFactoryId());

		product = productDbService.save(product);

		productResponseDto = ProductAdapter.buildResponseFromEntity(product);

		return productResponseDto;
	}

	@Override
	public ProductResponseDto activateDeactivateProduct(String id, boolean status) {

		log.info("Serving request to activate / deactivate a product with id:{}", id);

		Product product = productDbService.getProductById(id);

		if (Objects.isNull(product)) {
			String errorMsg = "Product with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		product = productDbService.activateDeactivateProduct(product, status);

		return ProductAdapter.buildResponseFromEntity(product);
	}

	@SuppressWarnings("unused")
	private void publishProduct(ProductResponseDto productResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, ProductResponseDto.class.getName(), productResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeProduct(ProductResponseDto productResponseDto) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		Product product = ProductAdapter.buildProductFromResponse(productResponseDto, factoryId);

		if (Objects.isNull(product)) {
			log.error("Unable to build product from response object: {}", productResponseDto);
			return;
		}

		productDbService.saveProduct(product);
	}

	@Override
	public void saveEntityFromCache(Map<String, ProductResponseDto> productByNameMap) {

		List<String> factories = factoryHttpService.getFactoryIds();

		if (CollectionUtils.isEmpty(factories)) {
			log.error("No active factories found in the system to populate data for");
			return;
		}

		factories.forEach(factoryId -> {

			log.info("Populating product data for factory id: {}", factoryId);

			productByNameMap.values().forEach(productResponseDto -> {

				productResponseDto.setFactoryId(factoryId);

				conditionalSaveProduct(productResponseDto);

			});
		});

	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && productDbService.isEntityExistsByName(name);
	}

	private void runValidations(ProductRequestDto productRequestDto) {

		validateName(productRequestDto);
	}

	private void validateName(ProductRequestDto productRequestDto) {

		if (isEntityExistsWithName(productRequestDto.getName())) {
			String errorMsg = "Product cannot be created/updated as record already exists with name: " + productRequestDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}

	@Override
	public Product findOrCreate(String name) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		Product product = ProductAdapter.buildProductFromName(name, factoryId);
		return productDbService.findOrCreate(product);
	}
}
