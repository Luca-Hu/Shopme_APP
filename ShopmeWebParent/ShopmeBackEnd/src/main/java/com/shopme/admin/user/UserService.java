package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;


@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 5; 
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAll(){ // list all users
		 return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){  // pagination for Users List
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		
		if(keyword != null) {
			return userRepo.findAll(keyword, pageable);
		}
		return userRepo.findAll(pageable);
	}
	
	public List<Role> listRoles(){ // list checkboxes list of Roles
		 return (List<Role>) roleRepo.findAll();
	}
	
	public User save(User user) {  // save user's basic information (except "photos" and "enable status")
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			User existedUser = userRepo.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existedUser.getPassword());
			}else {
				encodePassword(user);
			}
			
		}else {
			encodePassword(user);
		}
		return userRepo.save(user);
	}
	
	public void encodePassword(User user) { // encode the user's password then we could save encoded password to db
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {  // check Uniqueness of User Email
		User foundUserByEmail = userRepo.getUserByEmail(email);
		
		if(foundUserByEmail == null) {
			return true;
		}
		
		boolean isCreatingNew = (id == null);

		if (isCreatingNew && foundUserByEmail != null) {
			return false;
		} else if(!isCreatingNew && foundUserByEmail.getId() != id) {
			return false;
		}
		return true;
	}
	
	public User getUserById(Integer id) throws UserNotFoundException { 
		try {
		return userRepo.findById(id).get();
		}catch(NoSuchElementException ex){
			throw new UserNotFoundException("Could not find any user with ID:" + id);
		}
	}
	
	public void deleteUserById(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id); // the reason use counById method instead of getUserById: we don't want to return a full user object
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID:" + id);
		}
		
		userRepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepo.updateEnabledStatus(id, enabled);
	}
}

