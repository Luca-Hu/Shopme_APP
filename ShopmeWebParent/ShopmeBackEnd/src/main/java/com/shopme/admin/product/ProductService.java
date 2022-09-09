package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shope.common.exception.ProductNotFoundException;
import com.shopme.common.entity.Product;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 8;
	
	@Autowired private ProductRepository repo;
	
	public List<Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword){  // pagination for Users List
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		
		if(keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		return repo.findAll(pageable);
	}
	
	
	public Product save(Product product) {
		if(product.getCreatedTime() == null) {
			product.setCreatedTime(new Date());
		}

		return repo.save(product);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		
		Product productFoundByName = repo.findByName(name);
		
		if (isCreatingNew && productFoundByName != null) {
			return "DuplicatedName";
		} else if (productFoundByName != null && productFoundByName.getId() != id) {
			return "DuplicatedName";	
		}
		return "OK";
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = repo.countById(id);
		
		if(countById == null || countById  == 0) {
			throw new ProductNotFoundException("Could not find any Product with ID:" + id);
		}
		
		repo.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any Product with ID:" + id);
		}
	}
}
