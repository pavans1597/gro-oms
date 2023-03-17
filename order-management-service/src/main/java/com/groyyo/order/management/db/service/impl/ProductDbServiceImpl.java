package com.groyyo.order.management.db.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.ProductDbService;
import com.groyyo.order.management.entity.Product;
import com.groyyo.order.management.repository.ProductRepository;

@Service
public class ProductDbServiceImpl extends AbstractJpaServiceImpl<Product, Long, ProductRepository> implements ProductDbService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	protected ProductRepository getJpaRepository() {
		return productRepository;
	}

	@Override
	public List<Product> getAllProducts(String factoryId) {
		return (!Objects.isNull(factoryId) ? productRepository.findByFactoryIdOrderByNameAsc(factoryId)
				: productRepository.findByOrderByNameAsc());
	}

	@Override
	public List<Product> getAllProductsForStatus(boolean status, String factoryId) {
		return (!Objects.isNull(factoryId) ? productRepository.findByStatusAndFactoryIdOrderByNameAsc(status, factoryId)
				: productRepository.findByStatusOrderByNameAsc(status));
	}

	@Override
	public Product getProductById(String id) {
		return productRepository.findByUuid(id);
	}

	@Override
	public Product saveProduct(Product product) {
		return productRepository.saveAndFlush(product);
	}

	@Override
	public Product activateDeactivateProduct(Product product, boolean status) {
		product.setStatus(status);
		return productRepository.saveAndFlush(product);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(productRepository.findByName(name));
	}

	@Override
	public Product findOrCreate(Product product) {
		Product entity = productRepository.findByName(product.getName());
		if (entity == null) {
			entity = product;
			save(entity);
		}
		return entity;
	}

	@Override
	public Product findByNameAndFactoryId(String name, String factoryId) {
		return productRepository.findByNameAndFactoryId(name, factoryId);
	}
}
