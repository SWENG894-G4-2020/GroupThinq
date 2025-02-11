package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BallotTest {

	private static final User USER = new User("mboyer87", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
	private static final Decision DECISION = new Decision("Test Decision", USER);
	private Ballot testBallot = new Ballot();
	private BallotOption ballotOption = new BallotOption("BK Lounge", testBallot, USER);

	Set<BallotOption> ballotOptions = new HashSet<>();
	
	@BeforeEach
	void setUp() {
		testBallot.setId(1L);
		testBallot.setDecision(DECISION);
		testBallot.setExpirationDate(new Date(1337L));
		testBallot.setCreatedDate(new Date(1111L));
		testBallot.setUpdatedDate(new Date(2222L));
		testBallot.setType(new BallotType(2L, "Ranked-Choice"));
		
		ballotOptions.add(ballotOption);
		testBallot.setOptions(ballotOptions);
	}
	
	@Test
	void constructor_worksProperly() {
		Ballot ballot = new Ballot(DECISION, new BallotType(), new Date(1337L));
		
		assertNotNull(ballot.getCreatedDate());
		assertEquals("mboyer87", ballot.getDecision().getOwner().getUserName());
		assertEquals(new Date(1337L), ballot.getExpirationDate());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, testBallot.getId());
		assertEquals(0, testBallot.getRankedPairWinners().size());
		assertEquals(0, testBallot.getVotes().size());
		assertEquals("mboyer87", testBallot.getDecision().getOwner().getUserName());
		assertEquals(new Date(1337L), testBallot.getExpirationDate());
		assertEquals(new Date(1111L), testBallot.getCreatedDate());
		assertEquals(new Date(2222L), testBallot.getUpdatedDate());
		assertEquals("Ranked-Choice", testBallot.getType().getName());
		assertEquals(ballotOption, testBallot.getOptions().toArray()[0]);
	}
	
	@Test
	void setters_workProperly() {
		testBallot.setId(2L);
		testBallot.setDecision(new Decision());
		testBallot.setCreatedDate(new Date(3333L));
		testBallot.setUpdatedDate(new Date(4444L));
		testBallot.setExpirationDate(new Date(5555L));
		testBallot.setType(new BallotType(1L, "Single-Choice"));
		
		assertNull(testBallot.getDecision().getOwner());
		assertEquals(2L, testBallot.getId());
		assertEquals(1L, testBallot.getType().getId());
		assertEquals(new Date(3333L), testBallot.getCreatedDate());
		assertEquals(new Date(4444L), testBallot.getUpdatedDate());
		assertEquals(new Date(5555L), testBallot.getExpirationDate());
	}
}
