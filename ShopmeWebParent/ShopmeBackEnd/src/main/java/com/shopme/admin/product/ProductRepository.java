package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
	
	public Product findByName(String name);
	
	public Long countById(Integer id); // to delete operation
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" + "OR p.description LIKE %?1%" + "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
}
