package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;

class UserRoleIdTest {

	public UserRoleId userRoleId;
		
	@Test
	void constructor_worksProperly() {
		// given
		User user = new User("jsmith",
				"password",
				"Smith",
				"John",
				"jsmith@gmail.com",
				new Date(1337L),
				new Date()
		);
		
		// when
		userRoleId = new UserRoleId(user, new Role());
		
		// then
		assertNull(userRoleId.getRole().getId());
		assertNull(userRoleId.getRole().getName());
		assertEquals("jsmith", userRoleId.getUser().getUserName());		
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		UserRoleId userRoleId = new UserRoleId();
		
		assertNull(userRoleId.getUser());
		assertNull(userRoleId.getRole());
	}
}
