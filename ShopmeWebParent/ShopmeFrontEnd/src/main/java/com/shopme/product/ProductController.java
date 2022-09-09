package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shope.common.exception.CategoryNotFoundException;
import com.shope.common.exception.ProductNotFoundException;
import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("c/{category_name}")
	public String viewCategoryFirstPage(@PathVariable("category_name") String name, Model model) throws CategoryNotFoundException {
		return viewCategoryByPage(name, 1, model);
	}
	
	@GetMapping("c/{category_name}/page/{pageNum}")
	public String viewCategoryByPage(@PathVariable("category_name") String name, 
			@PathVariable("pageNum") int pageNum,
			Model model) throws CategoryNotFoundException {
		Category category = categoryService.getCategory(name);

		List<Category> listCategoryParents = categoryService.getCategoryParents(category); // get breadcrumb
		Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
		List<Product> listProducts = pageProducts.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1; // count users that be displayed for
																				// every page
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum); // in order to locate current page in pagination
		model.addAttribute("totalPages", pageProducts.getTotalPages()); // in order to locate the last page in
																		// pagination
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements()); // count all products
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParents", listCategoryParents);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("category", category);

		return "product/products_by_category";
	}
	
	@GetMapping("p/{product_name}")
	public String viewProductDetail(@PathVariable("product_name") String name, Model model) {
		System.out.println("??!");
		try {
			System.out.println("??!");
			Product product = productService.getProduct(name);
			System.out.println("!!!");
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory()); // get breadcrumb
			
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getName());
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}
	
	@GetMapping("/search")  // product_search_res page1
	public String searchFirstPage(@Param("keyword") String keyword, Model model) {
		return searchByPage(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@Param("keyword") String keyword, 
			@PathVariable("pageNum") int pageNum, Model model) {
		Page<Product> pageProducts = productService.search(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();

		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_SEARCH_RES_PAGE + 1; // count users that be displayed for every page
		long endCount = startCount + ProductService.PRODUCTS_PER_SEARCH_RES_PAGE - 1;

		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum); // in order to locate current page in pagination
		model.addAttribute("totalPages", pageProducts.getTotalPages()); // in order to locate the last page in pagination
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements()); // count all products
		model.addAttribute("pageTitle", keyword + "-search result");
		
		model.addAttribute("listResult", listResult);
		model.addAttribute("keyword", keyword);
		
		return "product/product_search_result";
	}
}
