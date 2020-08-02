package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.psu.edu.sweng.capstone.backend.model.User;

class RankedPairWinnerDTOTest {

	private final User testUser = new User("mboyer", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
	private final Decision testDecision = new Decision("Test Decision", testUser);
	private final BallotType testBallotType = new BallotType(1L, "Single-Choice");
	private Ballot testBallot = new Ballot(testDecision, testBallotType, new Date());
	private final BallotOption winningBallotOption = new BallotOption("Winner", testBallot, testUser);
	private final BallotOption losingBallotOption = new BallotOption("Loser", testBallot, testUser);
	
	RankedPairWinnerDTO dto;
	
	@BeforeEach
	void setUp() {
		dto = RankedPairWinnerDTO.build(new RankedPairWinner(testBallot, winningBallotOption, losingBallotOption, 1L));
	}
	
	@Test
	void getters_workProperly() {
		assertEquals("Winner", dto.getWinner().getTitle());
		assertEquals("Loser", dto.getLoser().getTitle());
		assertEquals(1L, dto.getMargin());
	}
	
	@Test
	void setters_workProperly() {
		BallotOption newWinner = new BallotOption("Winner Winner", testBallot, testUser);
		BallotOption newLoser = new BallotOption("Chicken Dinner", testBallot, testUser);
		final Long newMargin = 1337L;
		
		dto.setWinner(BallotOptionDTO.build(newWinner));
		dto.setLoser(BallotOptionDTO.build(newLoser));
		dto.setMargin(newMargin);
		
		assertEquals("Winner Winner", dto.getWinner().getTitle());
		assertEquals("Chicken Dinner", dto.getLoser().getTitle());
		assertEquals(1337L, dto.getMargin());
	}
	
	@Test
	void build_handlesNullValuesProperly() {
		dto = RankedPairWinnerDTO.build(new RankedPairWinner(testBallot, null, null, null));
		
		assertNull(dto.getWinner());
		assertNull(dto.getLoser());
		assertNull(dto.getMargin());
	}
}
