package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

class DecisionUserIdTest {

	private DecisionUserId decisionUserID;
	
	private Long decisionId1;
	private Long decisionId2;
	
	private Long userId1;
	private Long userId2;
		
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
	
    @Test
    void hashCode_worksProperly() {
		Long decisionId1 = 1L;
		Long userId1 = 2L;
		
		Long decisionId2 = 3L;
		Long userId2 = 4L;
		
        new EqualsTester()
            .addEqualityGroup(
                new DecisionUserId(decisionId1, userId1))
            .addEqualityGroup(
                new DecisionUserId(decisionId2, userId2))
            .testEquals();
    }
    
    @Test
    void equals_differentDecisions() {
    	buildEqualsIds(1L, 2L, 3L, 3L);
    	
    	DecisionUserId decUser1 = new DecisionUserId(decisionId1, userId1);
    	DecisionUserId decUser2 = new DecisionUserId(decisionId2, userId2);
    	
    	assertFalse(decUser1.equals(decUser2));
    }
    
    @Test
    void equals_sameDecisions() {
    	buildEqualsIds(1L, 1L, 3L, 3L);
    	
    	DecisionUserId decUser1 = new DecisionUserId(decisionId1, userId1);
    	DecisionUserId decUser2 = new DecisionUserId(decisionId2, userId2);
    	
    	assertTrue(decUser1.equals(decUser2));
    }
    
    @Test
    void equals_differentUsers() {
    	buildEqualsIds(1L, 1L, 3L, 4L);
    	
    	DecisionUserId decUser1 = new DecisionUserId(decisionId1, userId1);
    	DecisionUserId decUser2 = new DecisionUserId(decisionId2, userId2);
    	
    	assertFalse(decUser1.equals(decUser2));
    }
    
    @Test
    void equals_sameUsers() {
    	buildEqualsIds(1L, 1L, 3L, 3L);
    	
    	DecisionUserId decUser1 = new DecisionUserId(decisionId1, userId1);
    	DecisionUserId decUser2 = new DecisionUserId(decisionId2, userId2);
    	
    	assertTrue(decUser1.equals(decUser2));
    }
    
    private void buildEqualsIds(long d1, long d2, long u1, long u2) {    	
    	decisionId1 = d1;
    	decisionId2 = d2;
    	
    	userId1 = u1;
    	userId2 = u2;
	}
}
