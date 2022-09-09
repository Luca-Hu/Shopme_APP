package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shope.common.exception.ProductNotFoundException;
import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 10;
	public static final int PRODUCTS_PER_SEARCH_RES_PAGE = 10;
	
	@Autowired
	private ProductRepository repo;

	public Page<Product> listByCategory(int pageNum, Integer categoryId){  // get products in category
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategory(categoryId, categoryIdMatch, pageable);
 	}
	
	public Product getProduct(String name) throws ProductNotFoundException {  // get breadcrumb
		Product product = repo.findByName(name);
		if(product == null) {
			throw new ProductNotFoundException("Could not find any product with name " + name);
		}
		
		return product;
	}
	
	public Page<Product> search(String keyword, int pageNum){ // get search result
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_SEARCH_RES_PAGE);
		return repo.search(keyword, pageable);
	}
}
