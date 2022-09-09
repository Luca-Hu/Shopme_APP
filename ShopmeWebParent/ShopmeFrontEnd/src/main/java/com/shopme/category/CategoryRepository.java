package com.shopme.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.name != null ORDER BY c.name ASC")
	public List<Category> findAll();
	
	@Query("SELECT c FROM Category c WHERE c.name = ?1")
	public Category findByName(String name);
}
