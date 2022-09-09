package com.shopme.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTests {

	@Autowired
	private CartItemRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 1;
		Integer productId = 2;
		
		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);
		
		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(2);
		
		CartItem savedItem = repo.save(newItem);
		
		assertThat(savedItem.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 1;
		List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));
		
		listItems.forEach(System.out::println);
		
		assertThat(listItems.size()).isEqualTo(2);
	}
	
	@Test
	public void testFindByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId = 1;
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNotNull();
	}
	
	@Test
	public void testUpdateQuantity() {
		Integer customerId = 1;
		Integer productId = 1;
		Integer quantity = 4;
		
		repo.updateQuantity(quantity, customerId, productId);
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item.getQuantity()).isEqualTo(4);
	}
	
	@Test
	public void testDeleteByCustomerAndProduct() {
		Integer customerId = 1;
		Integer productId = 2;
		
		repo.deleteByCustomerAndProduct(customerId, productId);
		
		CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
		
		assertThat(item).isNull();
	}
	
}
