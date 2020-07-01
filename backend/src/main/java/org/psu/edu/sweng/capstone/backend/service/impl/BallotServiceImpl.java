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
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotServiceImpl implements BallotService {
	
	@Autowired
	private BallotDAO ballotDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	private static final String ERROR_HEADER = "Ballot ";
	
	@Override
	public ResponseEntity<BallotDTO> createBallot(final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Optional<Decision> decision = decisionDao.findById(ballotDTO.getDecisionId());

		if (!decision.isPresent()) {
			throw new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString());
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
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Optional<Ballot> ballot = ballotDao.findById(ballotId);
			
		if (!ballot.isPresent()) {
			throw new EntityNotFoundException(ERROR_HEADER + ballotId.toString());
		}
		else {
			ballotDao.delete(ballot.get());
			
			response.attachGenericSuccess();
		}
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Optional<Ballot> ballot = ballotDao.findById(ballotId);
		
		if (!ballot.isPresent()) {
			throw new EntityNotFoundException(ERROR_HEADER + ballotId.toString());
		}
		else {
			response.getData().add(BallotDTO.build(ballot.get()));
			response.attachGenericSuccess();
		}
		
		return response;
	}

	@Override
	public ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballotDTO) throws EntityNotFoundException {
		ResponseEntity<BallotDTO> response = new ResponseEntity<>();
		
		final Optional<Ballot> ballot = ballotDao.findById(ballotId);
		final Optional<Decision> decision = decisionDao.findById(ballotDTO.getDecisionId());
		
		if (!ballot.isPresent()) {
			throw new EntityNotFoundException(ERROR_HEADER + ballotId.toString());
		}
		else if (!decision.isPresent()) {
			throw new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString());
		}
		else {
			Ballot b = ballot.get();
			
			if (ballotDTO.getExpirationDate() != null) { b.setExpirationDate(ballotDTO.getExpirationDate()); }
			
			b.setUpdatedDate(new Date());
			
			ballotDao.save(b);
			
			response.attachGenericSuccess();
			}
		
		return response;
	}
}
