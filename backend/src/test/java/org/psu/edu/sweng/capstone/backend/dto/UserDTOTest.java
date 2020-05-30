package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UserDTOTest {

	private UserDTO testUser = new UserDTO("testUser", "fakepw", "user", "test", "testUser@foo.bar", new Date(1337L));
	
	@Test
	void constructor_worksProperly() {
		// given
		String username = "mwboyer";
		String password = "fakepw";
		String lastName = "Boyer";
		String firstName = "Matt";
		String emailAddress = "mboyer87@gmail.com";
		Date birthDate = new Date(1337L);
		
		// when
		UserDTO newUser = new UserDTO(username, password, lastName, firstName, emailAddress, birthDate);
		
		// then
		assertEquals("Boyer", newUser.getLastName());
		assertEquals("fakepw", newUser.getPassword());
		assertEquals("Matt", newUser.getFirstName());
		assertEquals("mwboyer", newUser.getUserName());
		assertEquals("mboyer87@gmail.com", newUser.getEmailAddress());
		assertEquals(birthDate, newUser.getBirthDate());
	}
	
	@Test
	void getters_worksProperly() {
		assertEquals("user", testUser.getLastName());
		assertEquals("fakepw", testUser.getPassword());
		assertEquals("test", testUser.getFirstName());
		assertEquals("testUser", testUser.getUserName());
		assertEquals("testUser@foo.bar", testUser.getEmailAddress());
		assertEquals(new Date(1337L), testUser.getBirthDate());
	}
	
	@Test
	void setters_worksProperly() {
		// when
		testUser.setLastName("Boyer");
		testUser.setPassword("newPassword");
		testUser.setFirstName("Matt");
		testUser.setUserName("mwboyer");
		testUser.setEmailAddress("mboyer87@gmail.com");
		testUser.setBirthDate(new Date(100L));
		
		// then
		assertEquals("mwboyer", testUser.getUserName());
		assertEquals("newPassword", testUser.getPassword());
		assertEquals("Boyer", testUser.getLastName());
		assertEquals("Matt", testUser.getFirstName());
		assertEquals("mboyer87@gmail.com", testUser.getEmailAddress());
		assertEquals(new Date(100L), testUser.getBirthDate());
	}
}
