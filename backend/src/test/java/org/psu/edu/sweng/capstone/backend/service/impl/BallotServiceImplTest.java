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
import org.psu.edu.sweng.capstone.backend.dao.BallotVoteDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotTypeDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedWinnerDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotVoteDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.helper.RankedPairCalculator;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotVote;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
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
	private BallotVoteDAO ballotVoteDao;
	
	@Mock
	private RankedWinnerDAO rankedWinnerDao;
	
	@Mock
	private BallotOptionDAO ballotOptionDao;
	
	@Mock
	private DecisionUserDAO decisionUserDao;
	
	@Mock
	private BallotOptionService ballotOptionService;
	
	@Mock
	private RankedPairCalculator rankedPairCalculator;
	
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
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		ResponseEntity<String> response = ballotServiceImpl.castVote(resultDTO);
		
		// then
		assertCreatedSuccess(response);
		verify(ballotVoteDao, times(1)).save(Mockito.any(BallotVote.class));
	}
	
	@Test
	void castVote_happyPath_rankIncluded() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		resultDTO.get(0).setRank(2L);
		
		// when
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		ResponseEntity<String> response = ballotServiceImpl.castVote(resultDTO);
		
		// then
		assertCreatedSuccess(response);
		verify(ballotVoteDao, times(1)).save(Mockito.any(BallotVote.class));
	}
	
	@Test
	void castVote_noUser() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
	}
	
	@Test
	void castVote_noBallot() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
	}
	
	@Test
	void castVote_noBallotOption() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
				
		when(userDao.findByUserName(resultDTO.get(0).getUserName())).thenReturn(Optional.of(testUser));
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.castVote(resultDTO); });
	}
	
	@Test
	void retrieveAllVotes_happyPath() throws EntityNotFoundException {
		// given
		testBallot.getVotes().add(new BallotVote(testBallot, testBallotOption, testUser));
		
		// when
		ResponseEntity<?> response = ballotServiceImpl.retrieveAllVotes(testBallot);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void retrieveAllVotes_noResults() throws EntityNotFoundException {
		// when
		ResponseEntity<BallotVoteDTO> response = ballotServiceImpl.retrieveAllVotes(testBallot);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void retrieveRankedChoiceResults_happyPath() throws EntityNotFoundException {
		// given
		DecisionUser du = new DecisionUser(testDecision, testUser);
		ArrayList<DecisionUser> duList = new ArrayList<>();
		duList.add(du);
		testBallotOption.setId(0L);
		
		ArrayList<BallotVote> votes = new ArrayList<>();
		votes.add(new BallotVote(testBallot, testBallotOption, testUser));
		
		// when
		when(rankedWinnerDao.findByBallot(testBallot)).thenReturn(Optional.empty());
		when(decisionUserDao.findAllByDecision(testBallot.getDecision())).thenReturn(duList);
		when(ballotVoteDao.findAllByUserAndBallotOrderByRankAsc(du.getUser(), testBallot)).thenReturn(votes);
		when(ballotOptionDao.findById(testBallotOption.getId())).thenReturn(Optional.of(testBallotOption));
		ballotServiceImpl.retrieveRankedChoiceResults(testBallot);
		
		verify(rankedWinnerDao, times(1)).save(Mockito.any(RankedWinner.class));
	}
	
	@Test
	void retrieveRankedChoiceResults_rankedWinnerAlreadyExists() throws EntityNotFoundException {
		// when
		when(rankedWinnerDao.findByBallot(testBallot)).thenReturn(Optional.of(new RankedWinner(testBallot, testBallotOption)));
		ballotServiceImpl.retrieveRankedChoiceResults(testBallot);
		
		verify(rankedPairCalculator, times(0)).runAlgorithm(testBallot, new ArrayList<Long>(), new ArrayList<ArrayList<Long>>());
	}
	
	@Test
	void retrieveRankedChoiceResults_ballotOptionDoesNotExist() throws EntityNotFoundException {
		// when
		when(rankedWinnerDao.findByBallot(testBallot)).thenReturn(Optional.empty());
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.retrieveRankedChoiceResults(testBallot); });
	}
	
	@Test
	void updateVote_happyPath() throws EntityNotFoundException {
		// given
		BallotVote result = new BallotVote(testBallot, testBallotOption, testUser);
		result.setId(1L);
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		when(ballotVoteDao.findById(result.getId())).thenReturn(Optional.of(result));
		ResponseEntity<String> response = ballotServiceImpl.updateVote(resultDTO);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void updateVote_rankIncluded() throws EntityNotFoundException {
		// given
		BallotVote result = new BallotVote(testBallot, testBallotOption, testUser);		
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		resultDTO.get(0).setRank(1L);
		resultDTO.get(0).setId(1L);
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		when(ballotVoteDao.findById(resultDTO.get(0).getId())).thenReturn(Optional.of(result));
		ResponseEntity<String> response = ballotServiceImpl.updateVote(resultDTO);
		
		// then
		assertGenericSuccess(response);
	}
	
	@Test
	void updateVote_noBallotFound() throws EntityNotFoundException {
		// given
		BallotVote vote = new BallotVote(testBallot, testBallotOption, testUser);
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotVoteDao.findById(Mockito.anyLong())).thenReturn(Optional.of(vote));
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.of(testBallotOption));
		when(ballotDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noUserFound() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noBallotResultFound() throws EntityNotFoundException {
		// given
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_noBallotOptionFound() throws EntityNotFoundException {
		// given
		BallotVote vote = new BallotVote(testBallot, testBallotOption, testUser);
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotVoteDao.findById(Mockito.anyLong())).thenReturn(Optional.of(vote));
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// then
		assertThrows(EntityNotFoundException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}
	
	@Test
	void updateVote_ballotOption_notPartOfBallot() throws EntityNotFoundException {
		// given
		testBallot.getOptions().clear();
		BallotVote result = new BallotVote(testBallot, testBallotOption, testUser);
		result.setId(1L);
		ArrayList<BallotVoteDTO> resultDTO = createBallotResultDTO();
		
		// when
		when(ballotDao.findById(resultDTO.get(0).getBallotId())).thenReturn(Optional.of(testBallot));
		when(ballotOptionDao.findById(resultDTO.get(0).getBallotOptionId())).thenReturn(Optional.of(testBallotOption));
		when(ballotVoteDao.findById(result.getId())).thenReturn(Optional.of(result));
		
		// then
		assertThrows(AccessDeniedException.class, () -> { ballotServiceImpl.updateVote(resultDTO); });
	}

	private ArrayList<BallotVoteDTO> createBallotResultDTO() {
		ArrayList<BallotVoteDTO> results = new ArrayList<>();
		
		BallotVoteDTO resultDTO = new BallotVoteDTO();
		
		resultDTO.setId(1L);
		resultDTO.setBallotId(1L);
		resultDTO.setBallotOptionId(2L);
		resultDTO.setUserName("MBoyer");
		
		results.add(resultDTO);
		
		return results;
	}
}