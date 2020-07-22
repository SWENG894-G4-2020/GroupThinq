package org.psu.edu.sweng.capstone.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.service.impl.BallotServiceImpl;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BallotControllerTest {

	@InjectMocks
	private BallotController ballotController;
	
	@Mock
	private BallotServiceImpl ballotServiceImpl; 
	
	private MockMvc mockMvc;
	
	private static final Long BALLOT_ID = 1L;
	private static final BallotDTO BALLOT_DTO = new BallotDTO();
	private static final BallotResultDTO BALLOT_RESULT_DTO = new BallotResultDTO();

	@BeforeEach
	void setUp() {
		FormattingConversionService bean = new FormattingConversionService();
		mockMvc = standaloneSetup(ballotController).setConversionService(bean).build();
	}
	
	@Test
	void getBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(get("/ballot/{id}", BALLOT_ID)).andExpect(status().isOk());
		
		// then
		verify(ballotServiceImpl, times(1)).retrieveBallot(BALLOT_ID);
	}

	@Test
	void deleteBallot_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(delete("/ballot/{id}", BALLOT_ID)).andExpect(status().isOk());

		// then
		verify(ballotServiceImpl, times(1)).deleteBallot(BALLOT_ID);
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
		verify(ballotServiceImpl, times(1)).createBallot(Mockito.any(BallotDTO.class));
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
		verify(ballotServiceImpl, times(1)).updateBallot(Mockito.anyLong(), Mockito.any(BallotDTO.class));
	}
	
	@Test
	void retrieveResults_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(get("/ballot/{id}/results", BALLOT_ID)).andExpect(status().isOk());
		
		// then
		verify(ballotServiceImpl, times(1)).retrieveResults(BALLOT_ID);
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
		verify(ballotServiceImpl, times(1)).castVote(Mockito.any(BallotResultDTO.class));
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
		verify(ballotServiceImpl, times(1)).updateVote(Mockito.any(BallotResultDTO.class));
	}
}
