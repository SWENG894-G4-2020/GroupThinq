package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.User;

class UserDTOTest {
	
	private UserDTO testUser;
	
	@BeforeEach
	void setUp() {
		User user = new User("testUser", "fakepw", "user", "test", "testUser@foo.bar", new Date(1337L));
		testUser = UserDTO.buildUserDTO(user);
		
		testUser.setCreatedDate(new Date(2222L));
		testUser.setLastLoggedIn(new Date(11111L));
	}
	
	@Test
	void getters_worksProperly() {
		assertEquals("user", testUser.getLastName());
		assertEquals("fakepw", testUser.getPassword());
		assertEquals("test", testUser.getFirstName());
		assertEquals("testUser", testUser.getUserName());
		assertEquals("testUser@foo.bar", testUser.getEmailAddress());
		assertEquals(new Date(1337L), testUser.getBirthDate());
		assertEquals(new Date(2222L), testUser.getCreatedDate());
		assertEquals(new Date(11111L), testUser.getLastLoggedIn());
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
