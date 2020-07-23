package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;


@ExtendWith(MockitoExtension.class)
class BallotOptionServiceImplTest extends ServiceImplTest {

	@Mock
	private UserDAO userDao;
	
	@Mock
	private BallotDAO ballotDao;
	
	@Mock
	private BallotOptionDAO ballotOptionDao;
	
	@InjectMocks
	private BallotOptionServiceImpl ballotOptionServiceImpl;
	
	private static final User TEST_USER = new User("pop pop", "90210", "Wayne", "Clark", "123imfake@email.gov", new Date(911L));
	private static final Decision TEST_DECISION = new Decision("Test Decision", TEST_USER);

	private static Ballot testBallot = new Ballot(TEST_DECISION, new BallotType(), new Date(1337));

	private static final BallotOption TEST_BALLOT_OPTION = new BallotOption("Title", testBallot, TEST_USER);
	
	private BallotOptionDTO dto; 
	
	@BeforeEach
	void setUp() {
		testBallot.setId(1L);
		
		dto = BallotOptionDTO.build(TEST_BALLOT_OPTION);
		dto.setId(1L);
	}
	
	@Test
	void updateBallotOption_happyPath() throws EntityNotFoundException {
		// when
		when(ballotOptionDao.findById(dto.getId())).thenReturn(Optional.of(TEST_BALLOT_OPTION));
		when(ballotDao.findById(Mockito.anyLong())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotOptionDTO> response = ballotOptionServiceImpl.updateBallotOption(dto.getId(), dto);
		
		// then
		assertGenericSuccess(response);
		verify(ballotOptionDao, times(1)).save(TEST_BALLOT_OPTION);
	}
	
	@Test
	void updateBallotOption_noTitleAndDescriptionUpdate() throws EntityNotFoundException {
		// given
		BallotOptionDTO newDTO = BallotOptionDTO.build(new BallotOption(null, testBallot, TEST_USER));
		
		// when
		when(ballotOptionDao.findById(dto.getId())).thenReturn(Optional.of(TEST_BALLOT_OPTION));
		when(ballotDao.findById(Mockito.anyLong())).thenReturn(Optional.of(testBallot));
		ResponseEntity<BallotOptionDTO> response = ballotOptionServiceImpl.updateBallotOption(dto.getId(), newDTO);
		
		// then
		assertGenericSuccess(response);
		verify(ballotOptionDao, times(1)).save(TEST_BALLOT_OPTION);
	}
	
	@Test
	void updateBallotOption_noTitleAndDescriptionUpdate_throwsNoBallotFound() throws EntityNotFoundException {
		// when
		when(ballotOptionDao.findById(dto.getId())).thenReturn(Optional.of(TEST_BALLOT_OPTION));
		when(ballotDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.updateBallotOption(dto.getId(), dto); });
	}
	
	@Test
	void updateBallotOption_noTitleAndDescriptionUpdate_throwsNoBallotOptionFound() throws EntityNotFoundException {
		// when
		when(ballotOptionDao.findById(dto.getId())).thenReturn(Optional.empty());
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.updateBallotOption(dto.getId(), dto); });
	}
	
	@Test
	void updateBallotOption_nullBallotId() throws EntityNotFoundException {
		// given
		testBallot.setId(null);
		BallotOptionDTO newDTO = BallotOptionDTO.build(new BallotOption(null, testBallot, TEST_USER));
		
		// when
		when(ballotOptionDao.findById(Mockito.anyLong())).thenReturn(Optional.of(TEST_BALLOT_OPTION));
		ResponseEntity<BallotOptionDTO> response = ballotOptionServiceImpl.updateBallotOption(1L, newDTO);
		
		// then
		assertGenericSuccess(response);
		verify(ballotOptionDao, times(1)).save(TEST_BALLOT_OPTION);
	}
	
	@Test
	void createBallotOption_happyPath() throws EntityNotFoundException {
		// when
		when(ballotDao.findById(dto.getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(dto.getUserName())).thenReturn(Optional.of(TEST_USER));
		ResponseEntity<String> response = ballotOptionServiceImpl.createBallotOption(dto);
		
		// then
		assertCreatedSuccess(response);
	}
	
	@Test
	void createBallotOption_nullBallotId() throws EntityNotFoundException {
		dto.setBallotId(null);
	    assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.createBallotOption(dto); });
	}
	
	@Test
	void createBallotOption_nullBallotUsername() throws EntityNotFoundException {
		dto.setUserName(null);
		assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.createBallotOption(dto); });
	}
	
	@Test
	void createBallotOption_noBallotFound() throws EntityNotFoundException {
		when(ballotDao.findById(dto.getBallotId())).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.createBallotOption(dto); });
	}
	
	@Test
	void createBallotOption_noUsernameFound() throws EntityNotFoundException {
		when(ballotDao.findById(dto.getBallotId())).thenReturn(Optional.of(testBallot));
		when(userDao.findByUserName(dto.getUserName())).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { ballotOptionServiceImpl.createBallotOption(dto); });
	}
}
