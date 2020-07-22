package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotResultDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotResult;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;

@ExtendWith(MockitoExtension.class)
class BallotServiceImplTest extends ServiceImplTest {
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@Mock
	private BallotOptionDAO ballotOptionDao;
	
	@Mock
	private BallotResultDAO ballotResultDao;
	
	@Mock
	private BallotOptionService ballotOptionService;
	
	@InjectMocks
	private BallotServiceImpl ballotServiceImpl;
	
	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private Decision testDecision = new Decision("Test Decision", testUser);
	private Ballot testBallot = new Ballot(testDecision, new Date(1337));
	private BallotOption testBallotOption = new BallotOption("Title", testBallot, testUser);
	
	private BallotDTO testBallotDTO;
	
	@BeforeEach
	void setUp() {		
		// given
		testBallotOption.setId(1L);
		testBallot.getOptions().add(testBallotOption);
		
		testBallotDTO = BallotDTO.build(testBallot);
		testBallotDTO.setId(1L);
		testBallotDTO.setDecisionId(2L);
	}
	
	@Test
	void createBallot_happyPath() throws EntityNotFoundException {
		// when
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.ofNullable(testDecision));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.createBallot(testBallotDTO);
		
		// then
		assertCreatedSuccess(response);		
		verify(ballotDao, times(1)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void createBallot_noDecisionExists() throws EntityNotFoundException {
		// when
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.createBallot(testBallotDTO); });
		verify(ballotDao, times(0)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void createBallot_expirationDateNull() throws EntityNotFoundException {
		// given
		testBallotDTO.setExpirationDate(null);
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.createBallot(testBallotDTO); });
		verify(ballotDao, times(0)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void deleteBallot_happyPath() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.deleteBallot(testBallotDTO.getId());
		
		// then
		assertGenericSuccess(response);
		verify(ballotDao, times(1)).delete(testBallot);
	}
	
	@Test
	void deleteBallot_ballotNotPresent() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.empty());
	    
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.deleteBallot(testBallotDTO.getId()); });
		verify(ballotDao, times(0)).delete(Mockito.any(Ballot.class));
	}
	
	@Test
	void retrieveBallot_happyPath() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.retrieveBallot(testBallotDTO.getId());
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void retrieveBallot_ballotNotPresent() throws EntityNotFoundException {
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.retrieveBallot(testBallotDTO.getId()); });	
    }
	
	@Test
	void updateBallot_happyPath() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.updateBallot(testBallotDTO.getId(), testBallotDTO);
		
		// then
		assertGenericSuccess(response);
		verify(ballotDao, times(1)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void updateBallot_ballotNotPresent() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateBallot(testBallotDTO.getId(), testBallotDTO); });
		verify(ballotDao, times(0)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void updateBallot_expirationDateNull_noBallotOptions() throws EntityNotFoundException {
		// given
		testBallotDTO.setExpirationDate(null);
		
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.updateBallot(testBallotDTO.getId(), testBallotDTO);
		
		// then
		assertGenericSuccess(response);
		verify(ballotDao, times(1)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void castVote_happyPath() throws EntityNotFoundException {
		// given
		BallotResultDTO result = new BallotResultDTO();
		
		result.setBallotId(1L);
		result.setBallotOptionId(2L);
		result.setUserName("MBoyer");
		
		// when
		when(userDao.findByUserName(result.getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(result.getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(result.getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		ResponseEntity<String> response = ballotServiceImpl.castVote(result);
		
		// then
		assertCreatedSuccess(response);
		verify(ballotResultDao, times(1)).save(Mockito.any(BallotResult.class));
	}
	
	@Test
	void castVote_noUser() throws EntityNotFoundException {
		// given
		BallotResultDTO result = new BallotResultDTO();
		
		result.setBallotId(1L);
		result.setBallotOptionId(2L);
		result.setUserName("MBoyer");
				
		when(userDao.findByUserName(result.getUserName())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(result); });
	}
	
	@Test
	void castVote_noBallot() throws EntityNotFoundException {
		// given
		BallotResultDTO result = new BallotResultDTO();
		
		result.setBallotId(1L);
		result.setBallotOptionId(2L);
		result.setUserName("MBoyer");
				
		when(userDao.findByUserName(result.getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(result.getBallotId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(result); });
	}
	
	@Test
	void castVote_noBallotOption() throws EntityNotFoundException {
		// given
		BallotResultDTO result = new BallotResultDTO();
		
		result.setBallotId(1L);
		result.setBallotOptionId(2L);
		result.setUserName("MBoyer");
				
		when(userDao.findByUserName(result.getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(result.getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(result.getBallotOptionId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(result); });
	}
	
	@Test
	void retrieveResults_happyPath() throws EntityNotFoundException {
		// given
		ArrayList<BallotResult> ballotResults = new ArrayList<>();
		ballotResults.add(new BallotResult(testBallot, testBallotOption, testUser));
		
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		when(ballotResultDao.findAllByBallot(testBallot)).thenReturn(ballotResults);
		ResponseEntity<BallotResultDTO> response = ballotServiceImpl.retrieveResults(testBallot.getId());
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void retrieveResults_noResults() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.of(testBallot));
		when(ballotResultDao.findAllByBallot(testBallot)).thenReturn(new ArrayList<>());
		ResponseEntity<BallotResultDTO> response = ballotServiceImpl.retrieveResults(testBallot.getId());
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void retrieveResults_noBallot() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallot.getId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.retrieveResults(testBallot.getId()); });
	}
}
