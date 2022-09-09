package com.shopme.checkout;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;

@Service
public class CheckoutService {

	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, float shippingRate, Integer deliverDays) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
	
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
		float paymentTotal = productTotal + shippingCostTotal;

		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		checkoutInfo.setDeliverDays(deliverDays);
		
		return checkoutInfo;
	}
	
	private float calculateShippingCost(List<CartItem> cartItems, float shippingRate) {
		float shippingCostTotal = 0.0f;
		
		for (CartItem item : cartItems) {
			float weight = 50;
			float shippingCost = weight * item.getQuantity() * shippingRate;
			float maxShippingCostPerItem = 20;
			float actualShippingCost = shippingCost > maxShippingCostPerItem ? maxShippingCostPerItem : shippingCost;
			item.setShippingCost(actualShippingCost);
			shippingCostTotal += actualShippingCost;
		}
		
		return shippingCostTotal;
	}
	
	private float calculateProductTotal(List<CartItem> cartItems) {
		float total = 0.0f;
		
		for (CartItem item : cartItems) {
			total += item.getSubtotal();
		}
		
		return total;
	}
}
