package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired 
	private CustomerRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		if(customer != null) {
			System.out.println(customer.getEmail());
			return new CustomerUserDetails(customer); // If user has been saved in db, then get his details.
		}
		throw new UsernameNotFoundException("Could not find user with this email: " + email);
	}

}
