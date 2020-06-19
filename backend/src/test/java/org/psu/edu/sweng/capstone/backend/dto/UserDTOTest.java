package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;

class UserDTOTest {
	
	private UserDTO testUser;
	
	@BeforeEach
	void setUp() {
		User user = new User("testUser", "fakepw", "user", "test", "testUser@foo.bar", new Date(1337L));
		
		user.setCreatedDate(new Date(2222L));
		user.setUpdatedDate(new Date(3333L));
		user.setLastLoggedIn(new Date(11111L));
		user.getUserRoles().add(new UserRole(user, new Role()));
		
		testUser = UserDTO.build(user);
		testUser.setPassword("fakepw");
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
		assertEquals(new Date(3333L), testUser.getUpdatedDate());
		assertEquals(new Date(11111L), testUser.getLastLoggedIn());
		assertEquals(1, testUser.getUserRoles().size());
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
		testUser.setUpdatedDate(new Date(3333L));
		testUser.setLastLoggedIn(new Date(11111L));
		
		// then
		assertEquals("mwboyer", testUser.getUserName());
		assertEquals("newPassword", testUser.getPassword());
		assertEquals("Boyer", testUser.getLastName());
		assertEquals("Matt", testUser.getFirstName());
		assertEquals("mboyer87@gmail.com", testUser.getEmailAddress());
		assertEquals(new Date(100L), testUser.getBirthDate());
		assertEquals(new Date(2222L), testUser.getCreatedDate());
		assertEquals(new Date(3333L), testUser.getUpdatedDate());
		assertEquals(new Date(11111L), testUser.getLastLoggedIn());
	}
	
	@Test
	void buildDTO_returnsNullValues() {
		// given
		User user = new User(null, null, null, null, null, null);
		
		// when
		UserDTO dto = UserDTO.build(user);
		
		// then
		assertNull(dto.getUserName());
		assertNull(dto.getPassword());
		assertNull(dto.getLastName());
		assertNull(dto.getFirstName());
		assertNull(dto.getEmailAddress());
		assertNull(dto.getBirthDate());
		assertNull(dto.getCreatedDate());
		assertNull(dto.getUpdatedDate());
		assertNull(dto.getLastLoggedIn());
	}
}
