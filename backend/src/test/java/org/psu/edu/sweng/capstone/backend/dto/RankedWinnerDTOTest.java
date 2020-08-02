package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.psu.edu.sweng.capstone.backend.model.User;

class RankedWinnerDTOTest {
	
	private final User testUser = new User("mboyer", "password", "Boyer", "Matt", "mboyer87@gmail.com", new Date());
	private final Decision testDecision = new Decision("Test Decision", testUser);
	private final BallotType testBallotType = new BallotType(1L, "Single-Choice");
	private Ballot testBallot = new Ballot(testDecision, testBallotType, new Date());
	private final BallotOption testBallotOption = new BallotOption("Test", testBallot, testUser);
	
	private RankedWinnerDTO dto;
		
	@BeforeEach
	void setUp() {
		testBallot.setId(1L);
		testBallot.getRankedPairWinners().add(new RankedPairWinner(testBallot, testBallotOption, testBallotOption, 1L));
		
		dto = RankedWinnerDTO.build(testBallot, testBallotOption);
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, dto.getBallotId());
		assertEquals(BallotOptionDTO.build(testBallotOption).getTitle(), dto.getWinner().getTitle());
		assertEquals(1, dto.getRankedPairWinners().size());
	}
	
	@Test
	void setters_workProperly() {
		List<RankedPairWinnerDTO> rankedPairWinners = new ArrayList<>();
		
		dto.setBallotId(2L);
		dto.setWinner(BallotOptionDTO.build(new BallotOption("TestTest", testBallot, testUser)));
		dto.setRankedPairWinners(rankedPairWinners);
		
		assertEquals(2L, dto.getBallotId());
		assertEquals("TestTest", dto.getWinner().getTitle());
		assertEquals(0, dto.getRankedPairWinners().size());
	}
	
	@Test
	void build_RunsNullsProperly() {
		testBallot = null;
		
		dto = RankedWinnerDTO.build(testBallot, testBallotOption);
		
		assertNull(dto.getBallotId());
		assertNull(dto.getWinner());
		assertEquals(0, dto.getRankedPairWinners().size());
	}
}
