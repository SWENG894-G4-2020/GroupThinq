package org.psu.edu.sweng.capstone.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.impl.UserServiceImpl;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	private String userName = "TestUser";
	private UserDTO userDto = new UserDTO("TestUser", "fakepw", "User", "Test", "TestUser@foo.bar", new Date(1337L), new Date());
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserServiceImpl userServiceImpl;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		FormattingConversionService bean = new FormattingConversionService();
		mockMvc = standaloneSetup(userController).setConversionService(bean).build();
	}

	@Test
	void getUsers_callsRightServiceFunction() throws Exception {		
		// when
		mockMvc.perform(get("/users")).andExpect(status().isOk());
		
		// then
		verify(userServiceImpl, times(1)).getUsers();
	}
	
	@Test
	void getUser_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(get("/users/{username}", userName)).andExpect(status().isOk());
		
		// then
		verify(userServiceImpl, times(1)).getUser(userName);		
	}
	
	@Test
	void deleteUser_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(delete("/users/{username}", userName)).andExpect(status().isOk());
		
		// then
		verify(userServiceImpl, times(1)).deleteUser(userName);
	}
	
	@Test
	void createUser_callsRightServiceFunction() throws Exception {		
		// when
		mockMvc.perform(post("/users/{username}", userName, userDto)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isOk());
		
		// then
		verify(userServiceImpl, times(1)).createUser(Mockito.anyString(), Mockito.any(UserDTO.class));
	}
	
	@Test
	void updateUser_callsRightServiceFunction() throws Exception {
		// when
		mockMvc.perform(put("/users/{username}", userName, userDto)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto)))
				.andExpect(status().isOk());
		
		// then
		verify(userServiceImpl, times(1)).updateUser(Mockito.anyString(), Mockito.any(UserDTO.class));
	}
}
