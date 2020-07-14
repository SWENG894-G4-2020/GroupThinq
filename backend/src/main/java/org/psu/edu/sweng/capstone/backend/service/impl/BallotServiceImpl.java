package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
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
		
		if (ballotDTO.getExpirationDate() == null) { throw new EntityNotFoundException("Expiration Date"); }
		
		final Decision decision = decisionDao.findById(ballotDTO.getDecisionId()).orElseThrow(
				() -> new EntityNotFoundException("Decision " + ballotDTO.getDecisionId().toString()));
		
		Ballot ballot = new Ballot(decision, ballotDTO.getExpirationDate());
			
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
}
