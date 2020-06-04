package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UserDTOTest {

	private UserDTO testUser = new UserDTO("testUser", "fakepw", "user", "test", "testUser@foo.bar", new Date(1337L), new Date(1L), new Date(7859L));
	
	@Test
	void constructor_worksProperly() {
		// given
		String username = "mwboyer";
		String password = "fakepw";
		String lastName = "Boyer";
		String firstName = "Matt";
		String emailAddress = "mboyer87@gmail.com";
		Date birthDate = new Date(1337L);
		Date createdDate = new Date(1L);
		Date lastLoggedIn = new Date(7859L);
		
		// when
		UserDTO newUser = new UserDTO(username, password, lastName, firstName, emailAddress, birthDate, createdDate, lastLoggedIn);
		
		// then
		assertEquals("Boyer", newUser.getLastName());
		assertEquals("fakepw", newUser.getPassword());
		assertEquals("Matt", newUser.getFirstName());
		assertEquals("mwboyer", newUser.getUserName());
		assertEquals("mboyer87@gmail.com", newUser.getEmailAddress());
		assertEquals(birthDate, newUser.getBirthDate());
		assertEquals(createdDate, newUser.getCreatedDate());
		assertEquals(lastLoggedIn, newUser.getLastLoggedIn());
	}
	
	@Test
	void getters_worksProperly() {
		assertEquals("user", testUser.getLastName());
		assertEquals("fakepw", testUser.getPassword());
		assertEquals("test", testUser.getFirstName());
		assertEquals("testUser", testUser.getUserName());
		assertEquals("testUser@foo.bar", testUser.getEmailAddress());
		assertEquals(new Date(1337L), testUser.getBirthDate());
		assertEquals(new Date(1L), testUser.getCreatedDate());
		assertEquals(new Date(7859L), testUser.getLastLoggedIn());
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
		testUser.setCreatedDate(new Date(2222L));
		testUser.setLastLoggedIn(new Date(11111L));
		
		// then
		assertEquals("mwboyer", testUser.getUserName());
		assertEquals("newPassword", testUser.getPassword());
		assertEquals("Boyer", testUser.getLastName());
		assertEquals("Matt", testUser.getFirstName());
		assertEquals("mboyer87@gmail.com", testUser.getEmailAddress());
		assertEquals(new Date(100L), testUser.getBirthDate());
		assertEquals(new Date(2222L), testUser.getCreatedDate());
		assertEquals(new Date(11111L), testUser.getLastLoggedIn());
	}
}
