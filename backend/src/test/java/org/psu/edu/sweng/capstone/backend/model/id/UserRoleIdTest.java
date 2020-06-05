package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

class UserRoleIdTest {

	public UserRoleId userRoleId;
		
	@Test
	void constructor_worksProperly() {
		// given
		Long userId = 1L;
		Long roleId = 2L;
		
		// when
		userRoleId = new UserRoleId(userId, roleId);
		
		// then
		assertEquals((Long)1L, userRoleId.getUser());
		assertEquals((Long)2L, userRoleId.getRole());
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		UserRoleId userRoleId = new UserRoleId();
		
		assertNull(userRoleId.getUser());
		assertNull(userRoleId.getRole());
	}
}
