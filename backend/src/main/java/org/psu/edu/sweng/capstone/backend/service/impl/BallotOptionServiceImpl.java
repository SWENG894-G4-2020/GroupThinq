package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotOptionServiceImpl implements BallotOptionService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private BallotDAO ballotDao;
	
	@Autowired
	private DecisionUserDAO decisionUserDao;
	
	@Autowired
	private BallotOptionDAO ballotOptionDao;
	
	private static final String ERROR_HEADER = "Ballot Option ";
	
	@Override
	public ResponseEntity<BallotOptionDTO> updateBallotOption(Long ballotOptionId, BallotOptionDTO boDTO) throws EntityNotFoundException {
		ResponseEntity<BallotOptionDTO> response = new ResponseEntity<>();
		
		final BallotOption ballotOption = ballotOptionDao.findById(ballotOptionId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotOptionId));
		
		final Long ballotId = boDTO.getBallotId();
		if (ballotId != null) {
			final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
					() -> new EntityNotFoundException("Ballot " + ballotId.toString()));
			
			ballotOption.setBallot(ballot);
		}
		
		if (boDTO.getTitle() != null) { ballotOption.setTitle(boDTO.getTitle()); }
		
		ballotOption.setUpdatedDate(new Date());
		
		ballotOptionDao.save(ballotOption);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<String> createBallotOption(BallotOptionDTO ballotOption) throws EntityNotFoundException {
		ResponseEntity<String> response = new ResponseEntity<>();
		
		final Long ballotId = ballotOption.getBallotId();
		final String ballotUsername = ballotOption.getUserName();
		
		if (ballotId == null) { throw new EntityNotFoundException("Ballot ID of Ballot Option"); }
		if (ballotUsername == null) { throw new EntityNotFoundException("Username of Ballot Option Owner"); }
		
		final Ballot ballot = ballotDao.findById(ballotOption.getBallotId()).orElseThrow(
				() -> new EntityNotFoundException("Ballot " + ballotId.toString()));
				
		final User user = userDao.findByUserName(ballotUsername).orElseThrow(
				() -> new EntityNotFoundException("User " + ballotUsername));
		
		Optional<DecisionUser> du = decisionUserDao.findByUserAndDecision(user, ballot.getDecision());
		
		if (!du.isPresent()) {
			throw new AccessDeniedException("User " + ballotUsername + 
					" is not on the Decision User list for Decision " + ballot.getDecision().getId() + ".");
		}
		
		ballot.getOptions().add(new BallotOption(ballotOption.getTitle(), ballot, user));
		
		response.attachCreatedSuccess();
		
		return response;
	}
}
