package com.shopme.shoppingcart;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private CartItemRepository cartRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException { // add product(s) in cart as cartItem(s)
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		if(cartItem != null) { // if this item has not existed in cart
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Number of items added to your cart cannot exceed 5. Please try again.");
			}
		} else { // if this item has existed in cart
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);
		
		cartRepo.save(cartItem);
		
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer) { // list cartItem(s) in cart-page
		return cartRepo.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) { // update cartItem's quantity in cart-page
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepo.findById(productId).get();
		float subtotal = product.getPrice() * quantity;
		return subtotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) { // remove cartItem(s) in cart-page
		cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
	public void deleteByCustomer(Customer customer) { // remove all cartItem(s) after customer places order
		cartRepo.deleteByCustomer(customer.getId());
	}
}
