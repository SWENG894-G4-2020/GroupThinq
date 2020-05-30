package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;

class DecisionUserIdTest {

	public DecisionUserId decisionUserID;
		
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
		decisionUserID = new DecisionUserId(new Decision(1L, "Decision"), user);
		
		// then
		assertEquals((Long)1L, decisionUserID.getDecision().getId());
		assertEquals("Decision", decisionUserID.getDecision().getName());
		assertEquals("jsmith", decisionUserID.getUser().getUserName());		
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		DecisionUserId defaultDecisionUserId = new DecisionUserId();
		
		assertNull(defaultDecisionUserId.getDecision());
		assertNull(defaultDecisionUserId.getUser());
	}
}
