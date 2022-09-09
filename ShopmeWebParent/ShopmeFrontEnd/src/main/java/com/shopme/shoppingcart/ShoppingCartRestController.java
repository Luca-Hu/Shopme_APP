package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shope.common.exception.CustomerNotFoundException;
import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

@RestController
public class ShoppingCartRestController {
	
	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name="productId") Integer productId,
			@PathVariable(name="quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQantity = cartService.addProduct(productId, quantity, customer);
			return updatedQantity + " item(s) of this product has added to cart successfully !";
		} catch (CustomerNotFoundException e) {
			return "Please sign in!";
		} catch(ShoppingCartException ex) {
			return ex.getMessage();
		}

	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException { // test if the customer from request has authenticated
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name="productId") Integer productId,
			@PathVariable(name="quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "Please sign in!";
		} 
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable(name="productId") Integer productId, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			
			return "The product has been removed from your shopping cart successfully.";
		} catch (CustomerNotFoundException e) {
			return "Please sign in!";
		} 
		
	}
}

