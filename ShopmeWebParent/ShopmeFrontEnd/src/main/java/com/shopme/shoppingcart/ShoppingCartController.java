package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

@Controller
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) { // No need exception check because customer have been logged in.
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		return customerService.getCustomerByEmail(email);
	}
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest req) {
		
		Customer customer = getAuthenticatedCustomer(req);
		
		List<CartItem> listCartItems = 
				cartService.listCartItems(customer);
		
		float subtotal = 0.0F;
		
		for(CartItem item : listCartItems) {
			subtotal += item.getSubtotal();
		}
		
		model.addAttribute("listCartItems", listCartItems);
		model.addAttribute("subtotal", subtotal);
		
		return "cart/shopping_cart";
	}
}
