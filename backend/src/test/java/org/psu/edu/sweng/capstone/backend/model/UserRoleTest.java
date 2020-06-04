package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRoleTest {
	
	private UserRole userRole = new UserRole();
	
	@BeforeEach
	void setUp() {
		userRole.setUser(new User("TestUser", "fakepw", "User", "Test", "TestUser@gmail.com", new Date(1337L)));
		userRole.setRole(new Role());
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		User newUser = new User("TReyob", "fakepw", "Reyob", "Ttam", "TtamReyob@gmail.com", new Date(1337L));
		Role newRole = new Role();
		
		// when
		UserRole userRole = new UserRole(newUser, newRole);
		
		// then
		assertNull(userRole.getRole().getId());
		assertNull(userRole.getRole().getName());
		assertEquals("TReyob", userRole.getUser().getUserName());
		assertEquals("Reyob", userRole.getUser().getLastName());
		assertEquals("Ttam", userRole.getUser().getFirstName());
		assertEquals("TtamReyob@gmail.com", userRole.getUser().getEmailAddress());
	}
	
	@Test
	void getters_workProperly() {
		assertNull(userRole.getRole().getId());
		assertNull(userRole.getRole().getName());
		assertEquals("TestUser", userRole.getUser().getUserName());
		assertEquals("User", userRole.getUser().getLastName());
		assertEquals("Test", userRole.getUser().getFirstName());
		assertEquals("TestUser@gmail.com", userRole.getUser().getEmailAddress());
	}
	
	@Test
	void setters_workProperly() {
		// given
		Role newRole = new Role();
		User newTestUser = new User("jsmith", "fakepw", "Smith", "John", "JohnSmith@gmail.com", new Date(1337L));
		
		// when
		userRole.setRole(newRole);
		userRole.setUser(newTestUser);
		
		// then
		assertNull(userRole.getRole().getId());
		assertNull(userRole.getRole().getName());
		assertEquals("jsmith", userRole.getUser().getUserName());
		assertEquals("Smith", userRole.getUser().getLastName());
		assertEquals("John", userRole.getUser().getFirstName());
		assertEquals("JohnSmith@gmail.com", userRole.getUser().getEmailAddress());
	}
}
