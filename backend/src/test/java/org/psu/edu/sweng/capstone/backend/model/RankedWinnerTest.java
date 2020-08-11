package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RankedWinnerTest {
	
	private final User testUser = new User("mboyer", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
	private final Decision testDecision = new Decision("Test Decision", testUser);
	private final BallotType testBallotType = new BallotType(1L, "Single-Choice");
	private Ballot testBallot = new Ballot(testDecision, testBallotType, new Date());
	private final BallotOption testBallotOption = new BallotOption("Winner", testBallot, testUser);
	
	private RankedWinner rw;
	
	@BeforeEach
	void setUp() {
		testBallot.setId(1L);
		
		rw = new RankedWinner(testBallot, testBallotOption);
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, rw.getBallot().getId());
		assertEquals("Winner", rw.getWinner().getTitle());
	}
	
	@Test
	void setters_workProperly() {
		Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337L));
		BallotOption testBallotOption = new BallotOption("Loser", testBallot, testUser);
		
		rw.setBallot(testBallot);
		rw.setWinner(testBallotOption);
		
		assertEquals(new Date(1337L), rw.getBallot().getExpirationDate());
		assertEquals("Loser", rw.getWinner().getTitle());
	}
	
	@Test
	void constructor_worksProperly() {
		Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337L));
		BallotOption testBallotOption = new BallotOption("Loser", testBallot, testUser);
		
		rw = new RankedWinner(testBallot, testBallotOption);
		
		assertEquals(new Date(1337L), rw.getBallot().getExpirationDate());
		assertEquals("Loser", rw.getWinner().getTitle());
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		RankedWinner rw = new RankedWinner();
		
		assertNull(rw.getBallot());
		assertNull(rw.getWinner());
	}
}
