package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

@ExtendWith(MockitoExtension.class)
class DecisionServiceImplTest {
	
	@Mock
	private DecisionDAO decisionDao;
	
	@InjectMocks
	private DecisionServiceImpl decisionServiceImpl;

	private Long decisionId = 1337L;
	private Decision dec = new Decision(1L, "Test Decision");
	private DecisionUser decUser = new DecisionUser(1L, dec, new User("TestUser", "fakepw", "User", "Test", "TestUser@gmail.com", new Date(1337L), new Date()));
	private Set<DecisionUser> decisionUsers = new HashSet<>();
		
	@BeforeEach
	void setUp() {
		// given
		decisionUsers.add(decUser);
		dec.setDecisionUsers(decisionUsers);
	}
	
	@Test
	void getUsers_returnsUsers_whenUsersExist() {
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		List<UserDTO> userList = decisionServiceImpl.getUsers(1337L);
		
		// then
		assertEquals(1, userList.size());
		assertEquals("TestUser", userList.get(0).getUserName());
	}
	
	@Test
	void getUsers_returnsEmptyList_whenNoUsersExist() {
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(null));
		List<UserDTO> userList = decisionServiceImpl.getUsers(1337L);
		
		// then
		assertEquals(0, userList.size());
	}
}
