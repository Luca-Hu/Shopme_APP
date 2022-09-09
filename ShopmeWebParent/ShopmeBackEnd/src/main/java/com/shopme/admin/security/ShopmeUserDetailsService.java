package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;

public class ShopmeUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Note: in my authentication, "username" is user's email
		User user = userRepo.getUserByEmail(email);
		if(user != null) {
			return new ShopmeUserDetails(user); // If user has been saved in db, then get his details.
		}
		throw new UsernameNotFoundException("Could not find user with this email: " + email);
	}

}
