package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.*;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.BallotExpiredException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotServiceImpl implements BallotService {
	
	@Autowired
	private BallotDAO ballotDao;

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	private static final String ERROR_HEADER = "Ballot ";
	
	@Override
	public ResponseEntity<BallotDTO> createBallot(final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		if (ballotDTO.getExpirationDate() == null) { throw new EntityNotFoundException("Expiration Date"); }
		
		final Decision decision = decisionDao.findById(ballotDTO.getDecisionId()).orElseThrow(
				() -> new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString()));
		
		Set<BallotOption> ballotOptions = new HashSet<>();
		for (BallotOptionDTO boDto : ballotDTO.getBallotOptions()) {
			final Ballot ballot = ballotDao.findById(boDto.getBallotId()).orElseThrow(
					() -> new EntityNotFoundException(ERROR_HEADER + boDto.getBallotId().toString()));
			
			final User user = userDao.findByUserName(boDto.getUserName()).orElseThrow(
					() -> new EntityNotFoundException("User " + boDto.getUserName()));
			
			BallotOption bo = new BallotOption(ballot, boDto.getExpirationDate(), user, boDto.getOptionTitle(), boDto.getOptionDescription());
			ballotOptions.add(bo);
		}
		
		Ballot ballot = new Ballot(decision, ballotDTO.getExpirationDate(), ballotOptions);
			
		ballotDao.save(ballot);
		
		response.attachCreatedSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));
			
		ballotDao.delete(ballot);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));
		
		response.getData().add(BallotDTO.build(ballot));
		response.attachGenericSuccess();
	
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));
		
		decisionDao.findById(ballotDTO.getDecisionId()).orElseThrow(
				() -> new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString()));
		
		if (ballotDTO.getExpirationDate() != null) { ballot.setExpirationDate(ballotDTO.getExpirationDate()); }
			
		ballot.setUpdatedDate(new Date());
		
		ballotDao.save(ballot);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<BallotOptionDTO> retrieveBallotOptions(Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotOptionDTO> response = new ResponseEntity<>();

		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));

		ballot.getBallotOptions().forEach(bo -> response.getData().add(BallotOptionDTO.build(bo)));
		response.attachGenericSuccess();

		return response;
	}

	@Override
	public ResponseEntity<BallotOptionDTO> addBallotOptions(Long ballotId, List<BallotOptionDTO> ballotOptions) throws EntityNotFoundException, BallotExpiredException {
		ResponseEntity<BallotOptionDTO> response = new ResponseEntity<>();

		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));
		final User user = userDao.findByUserName(ballotOptions.get(0).getUserName()).orElseThrow(
				() -> new EntityNotFoundException("User " + ballotOptions.get(0).getUserName()));
		for(BallotOptionDTO ballotOptionDTO: ballotOptions) {
			BallotOption bo = new BallotOption(ballot, ballotOptionDTO.getExpirationDate(), user, ballotOptionDTO.getOptionTitle(), ballotOptionDTO.getOptionDescription());
			if (ballot.getExpirationDate().after(new Date())) {
				ballotDao.getOne(ballotId).getBallotOptions().add(bo);
				response.attachGenericSuccess();
			}
		}
		if(response.getSuccess()) {
			return response;
		}
		throw new BallotExpiredException("Ballot " + ballotId);
	}

	@Override
	public ResponseEntity<BallotDTO> addVote(Long ballotId, Long ballotOptionId, String userName) throws EntityNotFoundException, BallotExpiredException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + ballotId.toString()));
		if(ballot.getExpirationDate().after(new Date())){
			ballotDao.getOne(ballotId).getBallotVotes().put(userName, new LinkedList<>(Arrays.asList(ballotOptionId)));
			response.attachGenericSuccess();
			return response;
		}
		throw new BallotExpiredException("Ballot " + ballotId);

	}
}
