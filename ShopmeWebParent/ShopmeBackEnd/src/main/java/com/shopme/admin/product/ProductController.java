package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shope.common.exception.ProductNotFoundException;
import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "name", "asc", null);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, 
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword);
		
		List<Product> listProducts = page.getContent();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1; // count users that be displayed for every page
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;
		
		model.addAttribute("currentPage", pageNum); // in order to locate current page in pagination
		model.addAttribute("totalPages", page.getTotalPages()); // in order to locate the last page in pagination
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements()); // count all users
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "products/products";
	}
	
	
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		Product product = new Product();
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0); // if we don't set the number to 0, null type can't add to integer 1 in "product_images.html"
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,			
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames
			) throws IOException {
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
			
		Product savedProduct = productService.save(product);
		
		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		ra.addFlashAttribute("message", "The product has been saved successfully.");
		
		return "redirect:/products";
	}

	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, 
			RedirectAttributes redirectAttributes) throws IOException {
		try {
			productService.delete(id);
			String productExtraImagesDir = "product-images/" + id + "/extras";
			String productImagesDir =  "product-images/" + id;

			AmazonS3Util.removeFolder(productExtraImagesDir);
			AmazonS3Util.removeFolder(productImagesDir);
			
			redirectAttributes.addFlashAttribute("message", "The Product (Id: " + id + ") has been deleted successfully!");	
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Edit Product Id: " + id);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			
			return "products/product_form";
		
		} catch(ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);
			 
			return "products/product_detail_modal";
		
		} catch(ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/products";
		}
	}

}
