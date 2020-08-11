package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

class UserRoleIdTest {

	private UserRoleId userRoleId;
	
	private Long userId1;
	private Long userId2;
	
	private Long roleId1;
	private Long roleId2;
		
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
	
    @Test
    void hashCode_worksProperly() {
		Long userId1 = 1L;
		Long roleId1 = 2L;
		
		Long userId2 = 3L;
		Long roleId2 = 4L;
		
        new EqualsTester()
            .addEqualityGroup(
                new UserRoleId(userId1, roleId1))
            .addEqualityGroup(
                new UserRoleId(userId2, roleId2))
            .testEquals();
    }
    
    @Test
    void equals_differentUsers() {
    	buildEqualsIds(1L, 2L, 3L, 3L);
    	
    	UserRoleId decUser1 = new UserRoleId(userId1, roleId1);
    	UserRoleId decUser2 = new UserRoleId(userId2, roleId2);
    	
    	assertFalse(decUser1.equals(decUser2));
    }
    
	@Test
    void equals_sameUsers() {
		buildEqualsIds(1L, 1L, 3L, 3L);
    	
    	UserRoleId decUser1 = new UserRoleId(userId1, roleId1);
    	UserRoleId decUser2 = new UserRoleId(userId2, roleId2);
    	
    	assertTrue(decUser1.equals(decUser2));
    }
    
    @Test
    void equals_differentRoles() {
    	buildEqualsIds(1L, 1L, 3L, 4L);
    	
    	UserRoleId decUser1 = new UserRoleId(userId1, roleId1);
    	UserRoleId decUser2 = new UserRoleId(userId2, roleId2);
    	
    	assertFalse(decUser1.equals(decUser2));
    }
    
    @Test
    void equals_sameRoles() {
    	buildEqualsIds(1L, 1L, 3L, 3L);
    	
    	UserRoleId decUser1 = new UserRoleId(userId1, roleId1);
    	UserRoleId decUser2 = new UserRoleId(userId2, roleId2);
    	
    	assertTrue(decUser1.equals(decUser2));
    }
    
    private void buildEqualsIds(long u1, long u2, long r1, long r2) {
    	userId1 = u1;
    	userId2 = u2;
    	
    	roleId1 = r1;
    	roleId2 = r2;
	}
}
