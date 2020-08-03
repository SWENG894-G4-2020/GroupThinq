package org.psu.edu.sweng.capstone.backend.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedWinnerDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BallotControllerTest {

	@InjectMocks
	private BallotController ballotController;
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private BallotService ballotService;
	
	@Mock
	private RankedWinnerDAO rankedWinnerDao;
	
	@Mock
	private BallotOptionService ballotOptionService;
	
	private MockMvc mockMvc;
	
	private static final Long BALLOT_ID = 1L;
	private static final BallotDTO BALLOT_DTO = new BallotDTO();
	private static final BallotOptionDTO BALLOT_OPTION_DTO = new BallotOptionDTO();
	private static final ArrayList<BallotResultDTO> BALLOT_RESULT_DTO = new ArrayList<>();
	
	private static final User TEST_USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private static final Decision TEST_DECISION = new Decision("Test Decision", TEST_USER);
	private static final BallotType TEST_BALLOT_TYPE = new BallotType(2L, "Ranked-Pair");
	private static Ballot testBallot = new Ballot(TEST_DECISION, TEST_BALLOT_TYPE, new Date(1337));
	private static final BallotOption TEST_BALLOT_OPTION = new BallotOption("Title", testBallot, TEST_USER);

	@BeforeEach
	void setUp() {
		FormattingConversionService bean = new FormattingConversionService();
		mockMvc = standaloneSetup(ballotController).setConversionService(bean).build();
		
		testBallot.setId(1L);
	}
	
	@Test
	void getBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(get("/ballot/{id}", BALLOT_ID)).andExpect(status().isOk());
		
		// then
		verify(ballotService, times(1)).retrieveBallot(BALLOT_ID);
	}

	@Test
	void deleteBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(delete("/ballot/{id}", BALLOT_ID)).andExpect(status().isOk());

		// then
		verify(ballotService, times(1)).deleteBallot(BALLOT_ID);
	}

	@Test
	void createBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(post("/ballot", BALLOT_DTO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(BALLOT_DTO)))
				.andExpect(status().isCreated());

		// then
		verify(ballotService, times(1)).createBallot(Mockito.any(BallotDTO.class));
	}

	@Test
	void updateBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(put("/ballot/{id}", BALLOT_ID, BALLOT_DTO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(BALLOT_DTO)))
				.andExpect(status().isOk());

		// then
		verify(ballotService, times(1)).updateBallot(Mockito.anyLong(), Mockito.any(BallotDTO.class));
	}

	@Test
	void retrieveVotes_happyPath() throws Exception {
		// given
		User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
		Decision testDecision = new Decision("Test Decision", testUser);
		BallotType testBallotType = new BallotType(1L, "Single-Choice");
		Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337));
		testBallot.setId(1L);

		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		mockMvc.perform(get("/ballot/{id}/votes", BALLOT_ID)).andExpect(status().isOk());

		// then
		verify(ballotService, times(1)).retrieveAllVotes(testBallot);
	}

	@Test
	void retrieveVotes_ballotNotFound() throws Exception {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.empty());

		// then
		verify(ballotService, times(0)).retrieveAllVotes(testBallot);
		assertThrows(EntityNotFoundException.class, () -> { ballotController.retrieveVotes(BALLOT_ID); });
	}
	
	@Test
	void retrieveResults_singleChoice_happyPath() throws Exception {
		// given
		User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
		Decision testDecision = new Decision("Test Decision", testUser);
		BallotType testBallotType = new BallotType(1L, "Single-Choice");
		Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337));
		testBallot.setId(1L);

		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		mockMvc.perform(get("/ballot/{id}/results", BALLOT_ID)).andExpect(status().isOk());
		
		// then
		verify(ballotService, times(1)).retrieveAllVotes(testBallot);
	}
	
	@Test
	void retrieveResults_singleChoice_ballotNotFound() throws Exception {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.empty());
		
		// then
		verify(ballotService, times(0)).retrieveAllVotes(testBallot);
	    assertThrows(EntityNotFoundException.class, () -> { ballotController.retrieveResults(BALLOT_ID); });
	}
	
	@Test
	void retrieveResults_rankedChoice() throws Exception {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		when(rankedWinnerDao.findByBallot(testBallot)).thenReturn(Optional.of(new RankedWinner(testBallot, TEST_BALLOT_OPTION)));
		mockMvc.perform(get("/ballot/{id}/results", BALLOT_ID)).andExpect(status().isOk());
		
		// then
		verify(ballotService, times(1)).retrieveRankedChoiceResults(testBallot);
	}
	
	@Test
	void retrieveResults_rankedChoice_notFound() throws Exception {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		when(rankedWinnerDao.findByBallot(testBallot)).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { ballotController.retrieveResults(BALLOT_ID); });
	}
	
	@Test
	void castVote_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(post("/ballot/{id}/vote", BALLOT_RESULT_DTO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(BALLOT_RESULT_DTO)))
				.andExpect(status().isCreated());

		// then
		verify(ballotService, times(1)).castVote(Mockito.<BallotResultDTO>anyList());
	}
	
	@Test
	void createBallotOption_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(post("/ballot/{id}/option", BALLOT_OPTION_DTO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(BALLOT_OPTION_DTO)))
				.andExpect(status().isCreated());

		// then
		verify(ballotOptionService, times(1)).createBallotOption(Mockito.any(BallotOptionDTO.class));
	}
	
	@Test
	void updateVote_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(put("/ballot/{id}/vote", BALLOT_RESULT_DTO)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(BALLOT_RESULT_DTO)))
				.andExpect(status().isOk());

		// then
		verify(ballotService, times(1)).updateVote(Mockito.<BallotResultDTO>anyList());
	}
}
