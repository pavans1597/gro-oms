package com.groyyo.order.management.db.service;

import com.groyyo.core.sqlPostgresJpa.service.AbstractJpaService;
import com.groyyo.order.management.entity.Product;

import java.util.List;

public interface ProductDbService extends AbstractJpaService<Product, Long> {


    List<Product> getAllProducts(String factoryId);


    List<Product> getAllProductsForStatus(boolean status,String factoryId);


    Product getProductById(String id);


    Product saveProduct(Product product);


    Product activateDeactivateProduct(Product product, boolean status);


    boolean isEntityExistsByName(String name);

    Product findOrCreate(Product product);
}
