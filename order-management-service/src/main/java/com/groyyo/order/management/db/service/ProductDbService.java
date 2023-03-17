package com.groyyo.order.management.db.service;

import java.util.List;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Product;

public interface ProductDbService extends AbstractJpaService<Product, Long> {

	List<Product> getAllProducts(String factoryId);

	List<Product> getAllProductsForStatus(boolean status, String factoryId);

	Product getProductById(String id);

	Product saveProduct(Product product);

	Product activateDeactivateProduct(Product product, boolean status);

	boolean isEntityExistsByName(String name);

	Product findOrCreate(Product product);

	/**
	 * @param name
	 * @param factoryId
	 * @return
	 */
	Product findByNameAndFactoryId(String name, String factoryId);
}
