package com.shopme.checkout;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.ControllerHelper;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.order.OrderService;
import com.shopme.shoppingcart.ShoppingCartService;

@Controller
public class CheckoutController {
	
	@Autowired private ControllerHelper controllerHelper;
	@Autowired private ShoppingCartService cartService;
	@Autowired private CheckoutService checkoutService;
	@Autowired private OrderService orderService;
	
	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		Random rand = new Random();
		float shippingRate = (rand.nextFloat() + 1)/ 10;
		Integer deliverDays = rand.nextInt(7) + 1; // random number : 1-7 

		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate, deliverDays);
		
		model.addAttribute("customer", customer);
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("shippingRate", shippingRate);
		model.addAttribute("deliverDays", deliverDays);
		
		return "checkout/checkout";
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request)  {
		String paymentType = request.getParameter("paymentMethod");
		float shippingRate = Float.valueOf(request.getParameter("shippingRate"));
		Integer deliverDays = Integer.valueOf(request.getParameter("deliverDays"));
		
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
		
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
				
		List<CartItem> cartItems = cartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate, deliverDays);
		
		orderService.createOrder(customer, customer.getAddress(), cartItems, paymentMethod, checkoutInfo);
		cartService.deleteByCustomer(customer);
		
		return "checkout/order_completed";
	}
}
