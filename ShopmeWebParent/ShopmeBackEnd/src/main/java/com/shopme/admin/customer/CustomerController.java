package com.shopme.admin.customer;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shope.common.exception.CustomerNotFoundException;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model){
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, 
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
		
		List<Customer> listCustomers = page.getContent();
		
		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1; // count users that be displayed for every page
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;
		
		model.addAttribute("currentPage", pageNum); // in order to locate current page in pagination
		model.addAttribute("totalPages", page.getTotalPages()); // in order to locate the last page in pagination
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements()); // count all users
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "customers/customers";
	}

	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);

			model.addAttribute("customer", customer);

			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}

	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);

			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer ID:" + id);

			return "customers/customer_form";
		} catch (CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, 
			RedirectAttributes redirectAttributes) {
		customerService.save(customer);
		redirectAttributes.addFlashAttribute("message", "The customer ID: " + customer.getId() + " has been updated successfully!");
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			customerService.delete(id);
			redirectAttributes.addFlashAttribute("message", "The User ID: " + id + " has been deleted successfully!");
		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
	
}
