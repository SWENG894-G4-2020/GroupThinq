package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;

@ExtendWith(MockitoExtension.class)
class BallotServiceImplTest extends ServiceImplTest {
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@InjectMocks
	private BallotServiceImpl ballotServiceImpl;
	
	private User testUser = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private Decision testDecision = new Decision("Test Decision", "Test Description", testUser);
	private Ballot testBallot = new Ballot(testDecision, new Date(1337), new HashSet<>());
	
	private BallotDTO testBallotDTO = BallotDTO.build(testBallot);
	
	@BeforeEach
	void setUp() {		
		// given
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
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.of(testDecision));
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
	void updateBallot_decisionNotPresent() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.empty());
	    
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateBallot(testBallotDTO.getId(), testBallotDTO); });
		verify(ballotDao, times(0)).save(Mockito.any(Ballot.class));
	}
	
	@Test
	void updateBallot_expirationDateNull() throws EntityNotFoundException {
		// given
		testBallotDTO.setExpirationDate(null);
		
		// when
		when(ballotDao.findById(testBallotDTO.getId())).thenReturn(Optional.of(testBallot));
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.of(testDecision));
		ResponseEntity<BallotDTO> response = ballotServiceImpl.updateBallot(testBallotDTO.getId(), testBallotDTO);
		
		// then
		assertGenericSuccess(response);
		verify(ballotDao, times(1)).save(Mockito.any(Ballot.class));
	}
}
