package org.psu.edu.sweng.capstone.backend.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	private static final String USERNAME = "TestUser";
	private static final UserDTO USER_DTO = new UserDTO("TestUser", "User", "Test", "TestUser@foo.bar");
	
	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserServiceImpl userServiceImpl;
	
	@Test
	void getUsers_callsRightServiceFunction() {
		userController.getUsers();
		verify(userServiceImpl, times(1)).getUsers();
	}
	
	@Test
	void getUser_callsRightServiceFunction() {
		userController.getUser(USERNAME);
		verify(userServiceImpl, times(1)).getUser(USERNAME);		
	}
	
	@Test
	void deleteUser_callsRightServiceFunction() {
		userController.deleteUser(USERNAME);
		verify(userServiceImpl, times(1)).deleteUser(USERNAME);
	}
	
	@Test
	void createUser_callsRightServiceFunction() {
		userController.createUser(USERNAME, USER_DTO);
		verify(userServiceImpl, times(1)).createUser(USERNAME, USER_DTO);
	}
	
	@Test
	void updateUser_callsRightServiceFunction() {
		userController.updateUser(USERNAME, USER_DTO);
		verify(userServiceImpl, times(1)).updateUser(USERNAME, USER_DTO);
	}
}
