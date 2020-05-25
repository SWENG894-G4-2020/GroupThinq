package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserDTOTest {

	private UserDTO testUser = new UserDTO("testUser", "user", "test", "testUser@foo.bar");
	
	@Test
	void constructor_worksProperly() {
		// given
		String username = "mwboyer";
		String lastName = "Boyer";
		String firstName = "Matt";
		String emailAddress = "mboyer87@gmail.com";
		
		// when
		UserDTO newUser = new UserDTO(username, lastName, firstName, emailAddress);
		
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
