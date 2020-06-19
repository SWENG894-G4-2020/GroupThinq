package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

@ExtendWith(MockitoExtension.class)
class DecisionServiceImplTest {
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@Mock
	private DecisionUserDAO decisionUserDao;
	
	@InjectMocks
	private DecisionServiceImpl decisionServiceImpl;

	private Long decisionId = 1337L;
	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private Decision dec = new Decision("Test Decision", "Test Description", new Date(), testUser);
	private DecisionUser decUser = new DecisionUser(dec, new User("TestUser", "fakepw", "User", "Test", "TestUser@gmail.com", new Date(1337L)));
	private Set<DecisionUser> decisionUsers = new HashSet<>();
		
	@BeforeEach
	void setUp() {
		// given
		decisionUsers.add(decUser);
		
		dec.setId(decisionId);
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
	
	@Test
	void getDecision_noDecisionExists() {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(null));
		DecisionDTO dto = decisionServiceImpl.getDecision(decisionId);
		
		// then
		assertNull(dto);
	}
	
	@Test
	void getDecision_decisionExists() {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		DecisionDTO dto = decisionServiceImpl.getDecision(decisionId);

		assertEquals("Test Decision", dto.getName());
	}
	
	@Test
	void updateDecision_noDecisionExists() {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.empty());
		String returnValue = decisionServiceImpl.updateDecision(decisionId, DecisionDTO.build(dec));
		
		assertEquals("Decision does not exist", returnValue);
	}
	
	@Test
	void updateDecision_decisionExists_hasNullValues() {
		// given
		Decision decision = new Decision(null, null, null, null);
		decision.setId(1L);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		String returnValue = decisionServiceImpl.updateDecision(decisionId, DecisionDTO.build(decision));
		
		// then
		assertEquals("Decision 1337 has been updated.", returnValue);
	}
	
	@Test
	void updateDecision_decisionExists_hasActualValues() {
		// given
		Decision decision = new Decision("Test Decision", "Test Description", new Date(1337L), testUser);
		decision.setId(1L);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		String returnValue = decisionServiceImpl.updateDecision(decisionId, DecisionDTO.build(decision));
		
		// then
		assertEquals("Decision 1337 has been updated.", returnValue);
	}
	
	@Test
	void createDecision_hasNoUser() {
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.empty());
		String returnValue = decisionServiceImpl.createDecision(DecisionDTO.build(dec));
		
		assertEquals("Decision could not be created.", returnValue);
	}
	
	@Test
	void createDecision_hasUser() {
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.ofNullable(testUser));
		String returnValue = decisionServiceImpl.createDecision(DecisionDTO.build(dec));
		
		assertEquals("Decision has been created.", returnValue);
	}
	
	@Test
	void deleteDecision_hasNoDecision() {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.empty());
		String returnValue = decisionServiceImpl.deleteDecision(dec.getId());

		assertEquals("Decision does not exist", returnValue);
	}
	
	@Test
	void deleteDecision_decisionExists_noUserDecisions() {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		when(decisionUserDao.findAllByDecision(dec)).thenReturn(new ArrayList<DecisionUser>());
		String returnValue = decisionServiceImpl.deleteDecision(dec.getId());

		assertEquals("Decision 1337 has been deleted.", returnValue);
	}
		
	@Test
	void deleteDecision_decisionExists_withUserDecisions() {
		// given
		ArrayList<DecisionUser> decisionUserList = new ArrayList<>();
		decisionUserList.add(new DecisionUser(dec, testUser));
				
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		when(decisionUserDao.findAllByDecision(dec)).thenReturn(decisionUserList);
		String returnValue = decisionServiceImpl.deleteDecision(dec.getId());

		// then
		assertEquals("Decision 1337 has been deleted.", returnValue);
	}
}
