package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RankedPairWinnerTest {

	private static final User TEST_USER = new User("mboyer", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
	private static final Decision TEST_DECISION = new Decision("Test Decision", TEST_USER);
	private static final BallotType TEST_BALLOT_TYPE = new BallotType(1L, "Single-Choice");
	
	private Ballot testBallot = new Ballot(TEST_DECISION, TEST_BALLOT_TYPE, new Date());
	
	private final BallotOption winningOption = new BallotOption("Winner", testBallot, TEST_USER);
	
	private final BallotOption losingOption = new BallotOption("Winner", testBallot, TEST_USER);
	
	private static final Long MARGIN = 1L;
	
	RankedPairWinner rpw;
	
	@BeforeEach
	void setUp() {
		testBallot.setId(1L);
		
		rpw = new RankedPairWinner(testBallot, winningOption, losingOption, MARGIN);
		rpw.setId(2L);
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, rpw.getBallot().getId());
		assertEquals(2L, rpw.getId());
		assertEquals(losingOption, rpw.getLoser());
		assertEquals(winningOption, rpw.getWinner());
		assertEquals(MARGIN, rpw.getMargin());
	}
	
	@Test
	void setters_workProperly() {
		Ballot testBallot = new Ballot(TEST_DECISION, TEST_BALLOT_TYPE, new Date());
		testBallot.setId(3L);
		
		BallotOption winningOption = new BallotOption("Chicken", testBallot, TEST_USER);
		BallotOption losingOption = new BallotOption("Dinner", testBallot, TEST_USER);
		
		Long margin = 5L;
		
		rpw.setBallot(testBallot);
		rpw.setWinner(winningOption);
		rpw.setLoser(losingOption);
		rpw.setMargin(margin);
		
		assertEquals(3L, rpw.getBallot().getId());
		assertEquals("Chicken", rpw.getWinner().getTitle());
		assertEquals("Dinner", rpw.getLoser().getTitle());
		assertEquals(5L, margin);
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		RankedPairWinner rpw = new RankedPairWinner();
		
		assertNull(rpw.getBallot());
		assertNull(rpw.getId());
		assertNull(rpw.getLoser());
		assertNull(rpw.getWinner());
		assertNull(rpw.getMargin());
	}
	
	@Test
	void constructor_worksProperly() {
		Ballot testBallot = new Ballot(TEST_DECISION, TEST_BALLOT_TYPE, new Date());
		testBallot.setId(3L);
		
		BallotOption winningOption = new BallotOption("Chicken", testBallot, TEST_USER);
		BallotOption losingOption = new BallotOption("Dinner", testBallot, TEST_USER);
		
		Long margin = 5L;
		
		rpw = new RankedPairWinner(testBallot, winningOption, losingOption, margin);
		
		assertEquals(3L, rpw.getBallot().getId());
		assertEquals("Chicken", rpw.getWinner().getTitle());
		assertEquals("Dinner", rpw.getLoser().getTitle());
		assertEquals(5L, margin);
	}
}
