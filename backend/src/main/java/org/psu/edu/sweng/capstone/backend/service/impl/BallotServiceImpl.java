package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotResultDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotTypeDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.BallotResult;
import org.psu.edu.sweng.capstone.backend.model.BallotType;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotServiceImpl implements BallotService {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private BallotDAO ballotDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	@Autowired
	private BallotTypeDAO ballotTypeDao;
	
	@Autowired
	private BallotResultDAO ballotResultDao;
	
	@Autowired
	private BallotOptionDAO ballotOptionDao;
	
	@Autowired
	private BallotOptionService ballotOptionService;
	
	private static final String BALLOT_HEADER = "Ballot ";
	private static final String BALLOT_OPTION_HEADER = "Ballot Option ";
	
	@Override
	public ResponseEntity<BallotDTO> createBallot(final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		if (ballotDTO.getExpirationDate() == null) { throw new EntityNotFoundException("Expiration Date"); }
		
		final Decision decision = decisionDao.findById(ballotDTO.getDecisionId()).orElseThrow(
				() -> new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString()));
		
		final BallotType type = ballotTypeDao.findById(ballotDTO.getBallotTypeId()).orElseThrow(
				() -> new EntityNotFoundException("Ballot Type " + ballotDTO.getBallotTypeId()));
		
		Ballot ballot = new Ballot(decision, type, ballotDTO.getExpirationDate());
			
		ballotDao.save(ballot);
		
		response.attachCreatedSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + ballotId.toString()));
			
		ballotDao.delete(ballot);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + ballotId.toString()));
		
		response.getData().add(BallotDTO.build(ballot));
		response.attachGenericSuccess();
	
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + ballotId.toString()));
				
		if (ballotDTO.getExpirationDate() != null) { ballot.setExpirationDate(ballotDTO.getExpirationDate()); }
		
		for (BallotOptionDTO bo : ballotDTO.getBallotOptions()) {
			ballotOptionService.updateBallotOption(bo.getId(), bo);
		}
		
		ballot.setUpdatedDate(new Date());
		
		ballotDao.save(ballot);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<String> castVote(final BallotResultDTO vote) throws EntityNotFoundException {
		ResponseEntity<String> response = new ResponseEntity<>();
		
		final User user = userDao.findByUserName(vote.getUserName()).orElseThrow(
				() -> new EntityNotFoundException("User " + vote.getUserName()));
		
		final Ballot ballot = ballotDao.findById(vote.getBallotId()).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + vote.getBallotId()));

		final BallotOption ballotOption = ballotOptionDao.findById(vote.getBallotOptionId()).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_OPTION_HEADER + vote.getBallotOptionId()));
		
		BallotResult result = new BallotResult(ballot, ballotOption, user);
		ballotResultDao.save(result);
				
		response.attachCreatedSuccess();
		
		return response;
	}
	
	@Override
	public ResponseEntity<String> updateVote(BallotResultDTO vote) throws EntityNotFoundException {
		ResponseEntity<String> response = new ResponseEntity<>();
			
		final Ballot ballot = ballotDao.findById(vote.getBallotId()).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + vote.getBallotId()));
		
		final User user = userDao.findByUserName(vote.getUserName()).orElseThrow(
				() -> new EntityNotFoundException("User " + vote.getUserName()));		

		BallotResult result = ballotResultDao.findByUserAndBallot(user, ballot)
				.orElseThrow( () -> new EntityNotFoundException("Ballot Result with Ballot " + vote.getBallotId() +
						", and User " + vote.getUserName()));
		
		final BallotOption ballotOption = ballotOptionDao.findById(vote.getBallotOptionId()).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_OPTION_HEADER + vote.getBallotOptionId()));
		
		if (ballot.getOptions().contains(ballotOption)) {
			result.setBallotOption(ballotOption);
			result.setVoteUpdatedDate(new Date());
			
			ballotResultDao.save(result);
		}
		else {
			throw new AccessDeniedException(BALLOT_OPTION_HEADER + vote.getBallotOptionId() + 
					" is not part of Ballot " + vote.getBallotId());
		}
		
		response.attachGenericSuccess();
		
		return response;
	}
	
	@Override
	public ResponseEntity<BallotResultDTO> retrieveResults(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotResultDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(BALLOT_HEADER + ballotId));
		
		ballotResultDao.findAllByBallot(ballot).stream().forEach(br -> response.getData().add(BallotResultDTO.build(br)));
		
		response.attachGenericSuccess();
		
		return response;
	}
}
