package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired 
	private CustomerRepository repo;
	
	@Test
	public void testCreateCustomer() {
		
		Customer customer = new Customer();
		customer.setFirstName("Rose");
		customer.setLastName("Camerl");
		customer.setPassword("tester");
		customer.setEmail("rose@gmail.com");
		customer.setPhoneNumber("972-462-7518");
		customer.setAddress("2681  East university Blvd");
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Baser";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		
		Customer updatedCustomer = repo.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 1;
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);		
		assertThat(findById).isNotPresent();		
	}
	
	
	@Test
	public void testFindByEmail() {
		String email = "david@gmail.com";
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);		
	}
	
	@Test
	public void testFindByVerificationCode() {
		String code = "002";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);		
	}
	
}
