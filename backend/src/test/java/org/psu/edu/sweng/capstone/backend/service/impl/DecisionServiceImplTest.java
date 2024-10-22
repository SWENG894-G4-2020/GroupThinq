package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotVoteDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotTypeDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedPairWinnerDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedWinnerDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotVote;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotService;

@ExtendWith(MockitoExtension.class)
class DecisionServiceImplTest extends ServiceImplTest {
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@Mock
	private BallotService ballotService;
	
	@Mock
	private BallotTypeDAO ballotTypeDao;
	
	@Mock
	private BallotVoteDAO ballotResultDao;
	
	@Mock
	private BallotOptionDAO ballotOptionDao;
	
	@Mock
	private DecisionUserDAO decisionUserDao;
	
	@Mock
	private RankedWinnerDAO rankedWinnerDao;
	
	@Mock
	private RankedPairWinnerDAO rankedPairWinnerDao;
		
	@InjectMocks
	private DecisionServiceImpl decisionServiceImpl;

	private Long decisionId = 1337L;
	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private Decision dec = new Decision("Test Decision", testUser);
	private DecisionUser decUser = new DecisionUser(dec, new User("TestUser", "fakepw", "User", "Test", "TestUser@gmail.com", new Date(1337L)));
	private ArrayList<DecisionUser> decisionUsers = new ArrayList<>();
		
	@BeforeEach
	void setUp() {       
		// given
		decisionUsers.add(decUser);
		
		dec.setId(decisionId);
	}
		
	@Test
	void getUsers_returnsUsers_whenUsersExist() throws EntityNotFoundException {
		// given
		ArrayList<DecisionUser> duList = new ArrayList<>();
		duList.add(new DecisionUser(dec, testUser));
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		when(decisionUserDao.findAllByDecision(dec)).thenReturn(duList);
		ResponseEntity<UserDTO> response = decisionServiceImpl.getUsers(1337L);
		
		// then
		assertEquals(1, response.getData().size());
		assertEquals("pop pop", response.getData().get(0).getUserName());
	}
	
	@Test
	void getUsers_returnsEmptyList_whenNoUsersExist() throws EntityNotFoundException {
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(null));
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.getUsers(1337L); });
	}
	
	@Test
	void getDecisions_returnsEmptyList() {
		// when
		when(decisionDao.findAll()).thenReturn(new ArrayList<>());
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.getDecisions();

		// then
		assertGenericSuccess(response);
		assertEquals(0, response.getErrors().size());
	}
	
	@Test
	void getDecisions_returnsResultsList() {
		// given
		ArrayList<Decision> results = new ArrayList<>();
		results.add(dec);
		
		// when
		when(decisionDao.findAll()).thenReturn(results);
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.getDecisions();

		// then
		assertGenericSuccess(response);
		assertEquals(0, response.getErrors().size());
		assertEquals(1, response.getData().size());
	}
	
	@Test
	void getDecision_noDecisionExists() throws EntityNotFoundException {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(null));
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.getDecision(decisionId); });
	}
	
	@Test
	void getDecision_decisionExists() throws EntityNotFoundException {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.getDecision(decisionId);

		assertEquals("Test Decision", response.getData().get(0).getName());
	}
	
	@Test
	void updateDecision_noDecisionExists() throws EntityNotFoundException {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.updateDecision(decisionId, DecisionDTO.build(dec)); });
    }
	
	@Test
	void updateDecision_decisionExists_hasNullValues_includedUsers() throws EntityNotFoundException {
		// given
		UserDTO userDTO = UserDTO.build(testUser);
		Decision decision = new Decision(null, null);
		DecisionDTO decisionDTO = DecisionDTO.build(decision);
		
		User otherUser = new User("opp opp", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
		
		ArrayList<DecisionUser> duList = new ArrayList<>();
		duList.add(new DecisionUser(decision, otherUser));
		
		decisionDTO.setId(1L);
		decisionDTO.getIncludedUsers().add(userDTO);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		when(userDao.findByUserName(userDTO.getUserName())).thenReturn(Optional.ofNullable(testUser));
		when(decisionUserDao.findAllByDecision(decision)).thenReturn(duList);
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.updateDecision(decisionId, decisionDTO);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void updateDecision_decisionExists_hasNullValues_includedUsers_notExistPreviously() throws EntityNotFoundException {
		// given
		UserDTO userDTO = UserDTO.build(testUser);
		Decision decision = new Decision(null, null);
		DecisionDTO decisionDTO = DecisionDTO.build(decision);
		
		ArrayList<DecisionUser> duList = new ArrayList<>();
		DecisionUser du = new DecisionUser(decision, testUser);
		duList.add(du);
		
		decisionDTO.setId(1L);
		decisionDTO.getIncludedUsers().add(userDTO);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		when(userDao.findByUserName(userDTO.getUserName())).thenReturn(Optional.ofNullable(testUser));
		when(decisionUserDao.findByUserAndDecision(testUser, decision)).thenReturn(Optional.of(du));
		when(decisionUserDao.findAllByDecision(decision)).thenReturn(duList);
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.updateDecision(decisionId, decisionDTO);
		
		// then
		assertEquals(200, response.getStatus());
		assertEquals(0, response.getErrors().size());
	}
	
	@Test
	void updateDecision_decisionExists_hasNullValues() throws EntityNotFoundException {
		// given
		UserDTO userDTO = UserDTO.build(new User("lmfao", "lul", "hehe", "haha", "test@gmail.com", new Date(1337L)));
		Decision decision = new Decision(null, null);
		DecisionDTO decisionDTO = DecisionDTO.build(decision);
		
		decisionDTO.setId(1L);
		decisionDTO.getIncludedUsers().add(userDTO);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		when(userDao.findByUserName(userDTO.getUserName())).thenReturn(Optional.ofNullable(testUser));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.updateDecision(decisionId, decisionDTO);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void updateDecision_decisionExists_hasNullValues_includedUserNotFound() throws EntityNotFoundException {
		// given
		UserDTO userDTO = UserDTO.build(new User("lmfao", "lul", "hehe", "haha", "test@gmail.com", new Date(1337L)));
		Decision decision = new Decision(null, null);
		DecisionDTO decisionDTO = DecisionDTO.build(decision);
		
		decisionDTO.setId(1L);
		decisionDTO.getIncludedUsers().add(userDTO);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		when(userDao.findByUserName(userDTO.getUserName())).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.updateDecision(decisionId, decisionDTO); });
	}
	
	@Test
	void updateDecision_decisionExists_hasActualValues() throws EntityNotFoundException {
		// given
		Decision decision = new Decision("Test Decision", testUser);
		decision.getBallots().add(new Ballot(decision, new BallotType(), new Date()));
		decision.setId(1L);
		decision.setDescription("Test Description");
		
		DecisionDTO decisionDTO = DecisionDTO.build(decision);
		
		decisionDTO.setIncludedUsers(null);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(decision));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.updateDecision(decisionId, decisionDTO);

		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void createDecision_hasNoUser() throws EntityNotFoundException {
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.createDecision(DecisionDTO.build(dec)); });
	}
	
	@Test
	void createDecision_hasNoIncludedUser() throws EntityNotFoundException {
		// given
		DecisionDTO dto = DecisionDTO.build(dec);
		dto = DecisionDTO.buildDecisionUserList(decisionUsers, dto);
		final DecisionDTO finalDTO = dto;
		
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.ofNullable(testUser));
		when(userDao.findByUserName(dto.getIncludedUsers().get(0).getUserName())).thenReturn(Optional.empty());

	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.createDecision(finalDTO); });	    
	}
		
	@Test
	void createDecision_hasUser_addsDecisionUsersAndBallotWithOptions() throws EntityNotFoundException {
		// give		
		Ballot testBallot = new Ballot(dec, new BallotType(), new Date());
		User newTestUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));

		testBallot.getOptions().add(new BallotOption("Title", testBallot, testUser));
		
		BallotType type = new BallotType(1L, "Single-Choice");
		testBallot.setType(type);
		
		dec.getBallots().add(testBallot);
		
		DecisionDTO dto = DecisionDTO.build(dec);
		dto = DecisionDTO.buildDecisionUserList(decisionUsers, dto);
		
		// when
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.ofNullable(testUser));
		when(ballotTypeDao.findById(type.getId())).thenReturn(Optional.of(type));
		when(userDao.findByUserName(dto.getOwnerUsername())).thenReturn(Optional.ofNullable(testUser));
		when(userDao.findByUserName(dto.getIncludedUsers().get(0).getUserName())).thenReturn(Optional.ofNullable(newTestUser));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.createDecision(dto);
		
		// then
		assertCreatedSuccess(response);
	}
	
	@Test
	void createDecision_hasUser_addsDecisionUsersAndBallotNoType() throws EntityNotFoundException {
		Ballot testBallot = new Ballot(dec, new BallotType(), new Date());
		testBallot.getOptions().add(new BallotOption("Title", testBallot, testUser));
		
		BallotType type = new BallotType(1L, "Single-Choice");
		testBallot.setType(type);
		
		dec.getBallots().add(testBallot);
		dec.setDescription("Description");
		
		DecisionDTO dto = DecisionDTO.build(dec);
		
		// when
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.ofNullable(testUser));
		when(ballotTypeDao.findById(type.getId())).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.createDecision(dto); });
	}
	
	@Test
	void createDecision_hasUser_noBallot() throws EntityNotFoundException {
		// given
		DecisionDTO dto = DecisionDTO.build(dec);
		
		// when
		when(userDao.findByUserName(testUser.getUserName())).thenReturn(Optional.ofNullable(testUser));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.createDecision(dto);
		
		// then
		assertCreatedSuccess(response);
	}
	
	@Test
	void deleteDecision_hasNoDecision() throws EntityNotFoundException {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { decisionServiceImpl.deleteDecision(dec.getId()); });
	}
	
	@Test
	void deleteDecision_decisionExists_noUserDecisions() throws EntityNotFoundException {
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.deleteDecision(dec.getId());

		assertGenericSuccess(response);
	}
		
	@Test
	void deleteDecision_decisionExists_withUserDecisionsAndBallotsAndResults() throws EntityNotFoundException {	
		// then
		Ballot testBallot = new Ballot(dec, new BallotType(), new Date());
		BallotOption testBallotOption = new BallotOption("Title", testBallot, testUser);
		
		testBallot.getOptions().add(testBallotOption);
		dec.getBallots().add(testBallot);
		
		BallotVote br = new BallotVote(testBallot, testBallotOption, testUser);

		ArrayList<BallotVote> results = new ArrayList<>();
		results.add(br);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.deleteDecision(dec.getId());

		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void deleteDecision_decisionExists_withUserDecisionsAndBallots_noResults() throws EntityNotFoundException {	
		// then
		Ballot testBallot = new Ballot(dec, new BallotType(), new Date());
		BallotOption testBallotOption = new BallotOption("Title", testBallot, testUser);
		
		testBallot.getOptions().add(testBallotOption);
		dec.getBallots().add(testBallot);
		
		// when
		when(decisionDao.findById(decisionId)).thenReturn(Optional.ofNullable(dec));
		ResponseEntity<DecisionDTO> response = decisionServiceImpl.deleteDecision(dec.getId());

		// then
		assertGenericSuccess(response);
	}
}
