package org.psu.edu.sweng.capstone.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.service.impl.DecisionServiceImpl;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class DecisionControllerTest {

	private Long decisionId = 1337L;
	private DecisionDTO decisionDto = new DecisionDTO();

	@InjectMocks
	private DecisionController decisionController;
	
	@Mock
	private DecisionServiceImpl decisionServiceImpl;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		FormattingConversionService bean = new FormattingConversionService();
		mockMvc = standaloneSetup(decisionController).setConversionService(bean).build();
	}

	@Test
	void getUsers_callsRightServiceFunction() throws Exception {
		
		// when
		mockMvc.perform(get("/decision/{id}/users", decisionId)).andExpect(status().isOk());
		
		// then
		verify(decisionServiceImpl, times(1)).getUsers(decisionId);
	}

	@Test
	void getDecision_callsRightServiceFunction() throws Exception {

		// when
		mockMvc.perform(get("/decision/{id}", decisionId)).andExpect(status().isOk());

		// then
		verify(decisionServiceImpl, times(1)).getDecision(decisionId);
	}

	@Test
	void deleteDecision_callsRightServiceFunction() throws Exception {

		// when
		mockMvc.perform(delete("/decision/{id}", decisionId)).andExpect(status().isOk());

		// then
		verify(decisionServiceImpl, times(1)).deleteDecision(decisionId);
	}

	@Test
	void createDecision_callsRightServiceFunction() throws Exception {

		// when
		mockMvc.perform(post("/decision", decisionDto)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(decisionDto)))
				.andExpect(status().isOk());

		// then
		verify(decisionServiceImpl, times(1)).createDecision(Mockito.any(DecisionDTO.class));
	}

	@Test
	void updateDecision_callsRightServiceFunction() throws Exception {

		// when
		mockMvc.perform(put("/decision/{id}", decisionId, decisionDto)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(decisionDto)))
				.andExpect(status().isOk());

		// then
		verify(decisionServiceImpl, times(1)).updateDecision(Mockito.anyLong(), Mockito.any(DecisionDTO.class));
	}

}
