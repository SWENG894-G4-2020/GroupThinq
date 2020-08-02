package org.psu.edu.sweng.capstone.backend.model.id;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

class RankedWinnerIdTest {

	private RankedWinnerId rankedWinnerId;
	
	private Long ballot1;
	private Long ballot2;
	
	private Long winner1;
	private Long winner2;
		
	@Test
	void constructor_worksProperly() {
		// given
		Long ballot = 1L;
		Long winner = 2L;
		
		// when
		rankedWinnerId = new RankedWinnerId(ballot, winner);
		
		// then
		assertEquals((Long)1L, rankedWinnerId.getBallot());
		assertEquals((Long)2L, rankedWinnerId.getWinner());
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		RankedWinnerId rankedWinnerId = new RankedWinnerId();
		
		assertNull(rankedWinnerId.getBallot());
		assertNull(rankedWinnerId.getWinner());
	}
	
    @Test
    void hashCode_worksProperly() {
		Long ballot1 = 1L;
		Long winner1 = 2L;
		
		Long ballot2 = 3L;
		Long winner2 = 4L;
		
        new EqualsTester()
            .addEqualityGroup(
                new RankedWinnerId(ballot1, winner1))
            .addEqualityGroup(
                new RankedWinnerId(ballot2, winner2))
            .testEquals();
    }
    
    @Test
    void equals_differentBallots() {
    	buildEqualsIds(1L, 2L, 3L, 3L);
    	
    	RankedWinnerId rankedWinner1 = new RankedWinnerId(ballot1, winner1);
    	RankedWinnerId rankedWinner2 = new RankedWinnerId(ballot2, winner2);
    	
    	assertFalse(rankedWinner1.equals(rankedWinner2));
    }
    
	@Test
    void equals_sameBallots() {
		buildEqualsIds(1L, 1L, 3L, 3L);
    	
		RankedWinnerId rankedWinner1 = new RankedWinnerId(ballot1, winner1);
		RankedWinnerId rankedWinner2 = new RankedWinnerId(ballot2, winner2);
    	
    	assertTrue(rankedWinner1.equals(rankedWinner2));
    }
    
    @Test
    void equals_differentOptions() {
    	buildEqualsIds(1L, 1L, 3L, 4L);
    	
    	RankedWinnerId rankedWinner1 = new RankedWinnerId(ballot1, winner1);
    	RankedWinnerId rankedWinner2 = new RankedWinnerId(ballot2, winner2);
    	
    	assertFalse(rankedWinner1.equals(rankedWinner2));
    }
    
    @Test
    void equals_sameOptions() {
    	buildEqualsIds(1L, 1L, 3L, 3L);
    	
    	RankedWinnerId rankedWinner1 = new RankedWinnerId(ballot1, winner1);
    	RankedWinnerId rankedWinner2 = new RankedWinnerId(ballot2, winner2);
    	
    	assertTrue(rankedWinner1.equals(rankedWinner2));
    }
    
    private void buildEqualsIds(long b1, long b2, long w1, long w2) {
    	ballot1 = b1;
    	ballot2 = b2;
    	
    	winner1 = w1;
    	winner2 = w2;
	}
}
