package com.shopme.customer;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/signup")
	public String showRegisterForm(Model model) {
		
		model.addAttribute("pageTitle", "Customer Sign Up");
		model.addAttribute("customer", new Customer());
		
		return "signup/signup_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request) {
		customerService.registerCustomer(customer);
		model.addAttribute("pageTitle", "Sign up Succeeded!");
		
		return "signup/signup_success";
	}
	
}
