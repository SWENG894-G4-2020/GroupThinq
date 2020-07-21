package org.psu.edu.sweng.capstone.backend.dto;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;

class BallotDTOTest {

    private static final User USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
    
    private Decision decision = new Decision("why is gamora?", USER);
    private Ballot ballot = new Ballot(decision, new Date(1337L));
    private BallotOption ballotOption = new BallotOption("BK Lounge", ballot, USER);
    private BallotDTO testDTO;
    private ArrayList<BallotOptionDTO> optionDTOs;
		
	@BeforeEach
	void setUp() {
		ballot.setId(1L);
		ballot.setUpdatedDate(new Date(1111L));		
		ballot.getOptions().add(ballotOption);
		
		decision.setId(2L);

		testDTO = BallotDTO.build(ballot);	
	}
	
	@Test
	void getters_workProperly() {
		assertNotNull(testDTO.getCreatedDate());
		assertEquals(1L, testDTO.getId());
		assertEquals(2L, testDTO.getDecisionId());
		assertEquals(new Date(1337L), testDTO.getExpirationDate());
		assertEquals(new Date(1111L), testDTO.getUpdatedDate());
		assertEquals(1, testDTO.getBallotOptions().size());
	}
	
	@Test
	void setters_workProperly() {
		// when
		testDTO.setId(2L);
		testDTO.setDecisionId(3L);
		testDTO.setCreatedDate(new Date(1111L));
		testDTO.setUpdatedDate(new Date(2222L));
		testDTO.setExpirationDate(new Date(3333L));
		testDTO.setBallotOptions(optionDTOs);

		// then
		assertEquals(2L, testDTO.getId());
		assertEquals(3L, testDTO.getDecisionId());
		assertEquals(new Date(1111L), testDTO.getCreatedDate());
		assertEquals(new Date(2222L), testDTO.getUpdatedDate());
		assertEquals(new Date(3333L), testDTO.getExpirationDate());
		assertEquals(optionDTOs, testDTO.getBallotOptions());
	}
	
	@Test
	void build_handlesNullsProperly() {
		// given
		Ballot testBallot = new Ballot(null, null);
		testBallot.setCreatedDate(null);
		
		// when
		BallotDTO testDTO = BallotDTO.build(testBallot);
		
		// then
		assertNull(testDTO.getCreatedDate());
		assertNull(testDTO.getDecisionId());
		assertNull(testDTO.getExpirationDate());
		assertNull(testDTO.getId());
		assertNull(testDTO.getUpdatedDate());

		assertEquals(0, testDTO.getBallotOptions().size());
	}
}
