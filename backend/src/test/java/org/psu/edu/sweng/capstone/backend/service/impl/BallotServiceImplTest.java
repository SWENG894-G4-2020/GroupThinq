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
import org.psu.edu.sweng.capstone.backend.dao.BallotTypeDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotResult;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class BallotServiceImplTest extends ServiceImplTest {
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@Mock
	private BallotTypeDAO ballotTypeDao;
	
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
	private BallotType testBallotType = new BallotType(1L, "Single-Choice");
	private Ballot testBallot = new Ballot(testDecision, testBallotType, new Date(1337));
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
		when(ballotTypeDao.findById(testBallotDTO.getBallotTypeId())).thenReturn(Optional.of(testBallotType));
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
	void createBallot_noBallotTypeExists() throws EntityNotFoundException {
		// when
		when(decisionDao.findById(testBallotDTO.getDecisionId())).thenReturn(Optional.ofNullable(testDecision));
		when(ballotTypeDao.findById(testBallotDTO.getBallotTypeId())).thenReturn(Optional.empty());
		
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
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		ResponseEntity<String> response = ballotServiceImpl.castVote(resultDTO);
		
		// then
		assertCreatedSuccess(response);
		verify(ballotResultDao, times(1)).save(Mockito.any(BallotResult.class));
	}
	
	@Test
	void castVote_noUser() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
	}
	
	@Test
	void castVote_noBallot() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
	}
	
	@Test
	void castVote_noBallotOption() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
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
	
	@Test
	void updateVote_happyPath() throws EntityNotFoundException {
		// given
		BallotResult result = new BallotResult(testBallot, testBallotOption, testUser);
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		when(ballotResultDao.findByUserAndBallot(testUser, testBallot)).thenReturn(Optional.of(result));
		ResponseEntity<String> response = ballotServiceImpl.updateVote(resultDTO);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void updateVote_noBallotFound() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noUserFound() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noBallotResultFound() throws EntityNotFoundException {
		// given
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotResultDao.findByUserAndBallot(testUser, testBallot)).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noBallotOptionFound() throws EntityNotFoundException {
		// given
		BallotResult result = new BallotResult(testBallot, testBallotOption, testUser);
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.empty());
		when(ballotResultDao.findByUserAndBallot(testUser, testBallot)).thenReturn(Optional.of(result));
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_ballotOption_notPartOfBallot() throws EntityNotFoundException {
		// given
		testBallot.getOptions().clear();
		BallotResult result = new BallotResult(testBallot, testBallotOption, testUser);
		ArrayList<BallotResultDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		when(ballotResultDao.findByUserAndBallot(testUser, testBallot)).thenReturn(Optional.of(result));
		
		// then
		assertThrows(AccessDeniedException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}

	private ArrayList<BallotResultDTO> createBallotResultDTO() {
		ArrayList<BallotResultDTO> results = new ArrayList<>();
		
		BallotResultDTO resultDTO = new BallotResultDTO();
		
		resultDTO.setBallotId(1L);
		resultDTO.setBallotOptionId(2L);
		resultDTO.setUserName("MBoyer");
		
		results.add(resultDTO);
		
		return results;
	}
}
