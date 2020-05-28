package org.psu.edu.sweng.capstone.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.service.impl.DecisionServiceImpl;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class DecisionControllerTest {

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
		// given
		Long decisionId = 1337L;
		
		// when
		mockMvc.perform(get("/decision/{id}", decisionId)).andExpect(status().isOk());
		
		// then
		verify(decisionServiceImpl, times(1)).getUsers(decisionId);
	}
}
