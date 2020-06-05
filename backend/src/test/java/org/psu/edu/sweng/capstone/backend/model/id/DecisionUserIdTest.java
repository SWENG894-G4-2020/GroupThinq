package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;

class DecisionUserIdTest {

	public DecisionUserId decisionUserID;
		
	@Test
	void constructor_worksProperly() {
		// given
		Long decisionId = 1L;
		Long userId = 2L;
		
		// when
		decisionUserID = new DecisionUserId(decisionId, userId);
		
		// then
		assertEquals((Long)1L, decisionUserID.getDecision());
		assertEquals((Long)2L, decisionUserID.getUser());		
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		DecisionUserId defaultDecisionUserId = new DecisionUserId();
		
		assertNull(defaultDecisionUserId.getDecision());
		assertNull(defaultDecisionUserId.getUser());
	}
}
