package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

class BallotResultIdTest {
	private BallotResultId ballotResultId;
	
	private Long ballotId1;
	private Long ballotId2;
	
	private Long ballotOptionId1;
	private Long ballotOptionId2;
	
	private Long userId1;
	private Long userId2;
		
	@Test
	void constructor_worksProperly() {
		// given
		Long ballotId = 1L;
		Long ballotOptionId = 2L;
		Long userId = 3L;
		
		// when
		ballotResultId = new BallotResultId(ballotId, ballotOptionId, userId);
		
		// then
		assertEquals((Long)1L, ballotResultId.getBallot());
		assertEquals((Long)2L, ballotResultId.getBallotOption());
		assertEquals((Long)3L, ballotResultId.getUser());
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		BallotResultId defaultBallotResultId = new BallotResultId();
		
		assertNull(defaultBallotResultId.getBallot());
		assertNull(defaultBallotResultId.getBallotOption());
		assertNull(defaultBallotResultId.getUser());
	}
	
    @Test
    void hashCode_worksProperly() {
		Long ballotId1 = 1L;
		Long ballotOptionId1 = 2L;
		Long userId1 = 3L;
		
		Long ballotId2 = 4L;
		Long ballotOptionId2 = 5L;
		Long userId2 = 6L;
		
        new EqualsTester()
            .addEqualityGroup(
                new BallotResultId(ballotId1, ballotOptionId1, userId1))
            .addEqualityGroup(
                new BallotResultId(ballotId2, ballotOptionId2, userId2))
            .testEquals();
    }
    
    @Test
    void equals_differentBallots() {
    	buildEqualsIds(1L, 2L, 4L, 4L, 3L, 3L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId2, ballotOptionId2, userId2);
    	
    	assertFalse(br1.equals(br2));
    }
    
    @Test
    void equals_sameBallots() {
    	buildEqualsIds(1L, 1L, 2L, 2L, 6L, 6L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId2, ballotOptionId2, userId2);
    	
    	assertTrue(br1.equals(br2));
    }
    
    @Test
    void equals_differentBallotOptions() {
    	buildEqualsIds(1L, 1L, 2L, 5L, 3L, 4L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId1, ballotOptionId2, userId2);
    	
    	assertFalse(br1.equals(br2));
    }
    
    @Test
    void equals_sameBallotOptions() {
    	buildEqualsIds(1L, 1L, 2L, 2L, 3L, 3L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId1, ballotOptionId2, userId2);
    	
    	assertTrue(br1.equals(br2));
    }
    
    @Test
    void equals_differentUsers() {
    	buildEqualsIds(1L, 1L, 2L, 2L, 3L, 4L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId1, ballotOptionId2, userId2);
    	
    	assertFalse(br1.equals(br2));
    }
    
    @Test
    void equals_sameUsers() {
    	buildEqualsIds(1L, 1L, 2L, 2L, 3L, 3L);
    	
    	BallotResultId br1 = new BallotResultId(ballotId1, ballotOptionId1, userId1);
    	BallotResultId br2 = new BallotResultId(ballotId1, ballotOptionId2, userId2);
    	
    	assertTrue(br1.equals(br2));
    }
    
    private void buildEqualsIds(Long b1, Long b2, Long bo1, Long bo2, Long u1, Long u2) {    	
    	ballotId1 = b1;
    	ballotId2 = b2;
    	
    	ballotOptionId1 = bo1;
    	ballotOptionId2 = bo2;
    	
    	userId1 = u1;
    	userId2 = u2;
	}
}
