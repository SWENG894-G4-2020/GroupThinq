package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	private User testUser = new User();
	
	@BeforeEach
	void setup() {	
		testUser.setLastName("user");
		testUser.setFirstName("test");
		testUser.setUserName("testUser");
		testUser.setEmailAddress("testUser@foo.bar");
	}
	
	@Test
	void constructor_worksProperly() {
		// given
		String username = "mwboyer";
		String lastName = "Boyer";
		String firstName = "Matt";
		String emailAddress = "mboyer87@gmail.com";
		
		// when
		User newUser = new User(username, lastName, firstName, emailAddress);
		
		// then
		assertEquals("Boyer", newUser.getLastName());
		assertEquals("Matt", newUser.getFirstName());
		assertEquals("mwboyer", newUser.getUserName());
		assertEquals("mboyer87@gmail.com", newUser.getEmailAddress());
	}
	
	@Test
	void getters_worksProperly() {
		assertEquals("user", testUser.getLastName());
		assertEquals("test", testUser.getFirstName());
		assertEquals("testUser", testUser.getUserName());
		assertEquals("testUser@foo.bar", testUser.getEmailAddress());
	}
	
	@Test
	void setters_worksProperly() {
		// when
		testUser.setLastName("Boyer");
		testUser.setFirstName("Matt");
		testUser.setUserName("mwboyer");
		testUser.setEmailAddress("mboyer87@gmail.com");
		
		// then
		assertEquals("Boyer", testUser.getLastName());
		assertEquals("Matt", testUser.getFirstName());
		assertEquals("mwboyer", testUser.getUserName());
		assertEquals("mboyer87@gmail.com", testUser.getEmailAddress());
	}
}
