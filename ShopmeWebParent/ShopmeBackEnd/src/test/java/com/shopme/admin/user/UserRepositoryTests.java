package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql=false) 
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)	 // Note : if (@Rollback == false), the test result change will persist.
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() { // Note : Hibernate will generate the table.
		Role roleAdmin = entityManager.find(Role.class, 1); 
		User userNamHm = new User("namhm@test.com", "tester", "Nam", "Hm");
		userNamHm.addRole(roleAdmin);
		
		User savedUser = repo.save(userNamHm);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() { // test user's multi-optional roles property
		User userRaven = new User("raven@test.com", "tester", "Raven", "kumer");
		Role roleSalesperson = new Role(2);
		Role roleShipper = new Role(3);
		userRaven.addRole(roleSalesperson);
		userRaven.addRole(roleShipper);
		
		User savedUser = repo.save(userRaven);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() { 
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user)); // Note: Need add "toString()" method in User/Roles class.
	}
	
	@Test
	public void testGetUserById() { 
		User theUser = repo.findById(1).get();
		System.out.println(theUser);
		assertThat(theUser).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() { 
		User theUser = repo.findById(1).get(); 
		theUser.setEnabled(true); // -> 1
		theUser.setEmail("updated@test.com");
		repo.save(theUser);
	}
	
	@Test
	public void testUpdateUserRoles() {  
		User theUser = repo.findById(3).get(); // Note: this test user need have salesperson role.
		Role roleAdmin = new Role(1);
		Role roleSalesperson = new Role(2);
		
		theUser.getRoles().remove(roleSalesperson);
		theUser.addRole(roleAdmin);
		repo.save(theUser);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "ana@test.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() { // Note: only get user id instead of get full user properties in db
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() { // switch user's "enable status" to "disable"
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {	// switch user's "enable status" to "enable"
		Integer id = 1;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() { // pagination for Users List
		int pageNumber = 1; // Note: value range [0, x)
		int pageSize = 5;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "ikun";
		
		int pageNumber = 0; // Note: value range [0, x)
		int pageSize = 10;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
}