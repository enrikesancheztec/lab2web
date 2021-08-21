/*
 * ProductManager
 * Version 1.0
 * August 14, 2021 
 * Copyright 2021 Tecnologico de Monterrey
 */
package mx.tec.web.lab.manager;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import mx.tec.web.lab.entity.Product;
import mx.tec.web.lab.entity.Sku;
import mx.tec.web.lab.repository.ProductRepository;


/**
 * The Product Manager with all the available business operations for the products
 * @author Enrique Sanchez
 * @version 1.0
 */
@Service
public class ProductManager {
	/** A reference to the Product Repository */
	@Resource
	private ProductRepository productRepository;	
	
	/**
	 * Retrieve all the products
	 * @return List of products
	 */
	public List<mx.tec.web.lab.entity.Product> getProducts() {
		return productRepository.findAll();
	}

	/**
	 * Retrieve an specific product based on a given product id
	 * @param id Product id
	 * @return Optional containing a product if the product was found or empty otherwise
	 */
	public Optional<Product> getProduct(final long id) {
		return productRepository.findById(id);
	}

	/**
	 * Retrieve an specific product based on a given product id
	 * @param pattern Pattern to search
	 * @return Optional containing a product if the product was found or empty otherwise
	 */
	public List<mx.tec.web.lab.entity.Product> getProducts(final String pattern) {
		return productRepository.findByNameLike(pattern);
	}
	
	/**
	 * Add a new Product to the product list based on a given product
	 * @param newProduct Product to add
	 * @return An Optional containing the new product
	 */
	public Product addProduct(final Product newProduct) {
		for (final Sku newSku : newProduct.getChildSkus()) {
			newSku.setParentProduct(newProduct);
		}

		return productRepository.save(newProduct);
	}
	
	/**
	 * Delete the product based on a given product
	 * @param existingProduct The product to delete
	 */
	public void deleteProduct(final Product existingProduct) {
		productRepository.delete(existingProduct);
	}
	
	/**
	 * Update an existing product based on a given modified product and a product id
	 * @param id The product id for the original product
	 * @param modifiedProduct The product new version
	 */
	public void updateProduct(final long id, final Product modifiedProduct) {
		final Optional<Product> existingProduct = getProduct(id);
		
		if (existingProduct.isPresent()) {
			for (final Sku modifiedSku : modifiedProduct.getChildSkus()) {
				modifiedSku.setParentProduct(modifiedProduct);
			}
			
			productRepository.save(modifiedProduct);
		}
	}		
}
