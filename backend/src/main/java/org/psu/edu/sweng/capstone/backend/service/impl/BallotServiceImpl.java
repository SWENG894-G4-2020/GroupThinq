package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.ResponseError;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotServiceImpl implements BallotService {
	
	@Autowired
	private BallotDAO ballotDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	private static final String BALLOT_NOT_FOUND_MESSAGE = "Ballot ";
	private static final Logger LOGGER = LoggerFactory.getLogger(BallotServiceImpl.class);
	
	@Override
	public ResponseEntity<BallotDTO> createBallot(BallotDTO ballotDTO) {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Decision> decision = decisionDao.findById(ballotDTO.getDecisionId());

			if (!decision.isPresent()) {
				response.attachEntityNotFound("Decision " + ballotDTO.getDecisionId().toString());
			}
			else if (ballotDTO.getExpirationDate() == null) {
				response.getErrors().add(new ResponseError(ErrorEnum.RESOURCE_CONFLICT, "Expiration Date missing to create Ballot"));
				response.setStatus(500);
				response.setSuccess(false);
			}
			else {
				Ballot ballot = new Ballot(decision.get(), ballotDTO.getExpirationDate());
				
				ballotDao.save(ballot);
				
				response.attachCreatedSuccess();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error creating Ballot on Decision {}", ballotDTO.getDecisionId(), e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> deleteBallot(Long ballotId) {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Ballot> ballot = ballotDao.findById(ballotId);
			
			if (!ballot.isPresent()) {
				response.attachEntityNotFound(BALLOT_NOT_FOUND_MESSAGE + ballotId.toString());
			}
			else {
				ballotDao.delete(ballot.get());
				
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error deleting Ballot {}", ballotId, e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> retrieveBallot(Long ballotId) {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Ballot> ballot = ballotDao.findById(ballotId);
			
			if (!ballot.isPresent()) {
				response.attachEntityNotFound(BALLOT_NOT_FOUND_MESSAGE + ballotId.toString());
			}
			else {
				response.getData().add(BallotDTO.build(ballot.get()));
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error retrieving Ballot {}", ballotId, e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> updateBallot(Long ballotId, BallotDTO ballotDTO) {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Ballot> ballot = ballotDao.findById(ballotId);
			Optional<Decision> decision = decisionDao.findById(ballotDTO.getDecisionId());
			
			if (!ballot.isPresent()) {
				response.attachEntityNotFound(BALLOT_NOT_FOUND_MESSAGE + ballotId.toString());
			}
			else if (!decision.isPresent()) {
				response.attachEntityNotFound("Decision " + ballotDTO.getDecisionId().toString());
			}
			else {
				Ballot b = ballot.get();
				
				if (ballotDTO.getExpirationDate() != null) { b.setExpirationDate(ballotDTO.getExpirationDate()); }
				
				b.setUpdatedDate(new Date());
				
				ballotDao.save(b);
				
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error updating Ballot {}", ballotId, e);
		}
		
		return response;
	}
}
