package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BallotOptionServiceImpl implements BallotOptionService {

	@Autowired
	private BallotDAO ballotDao;
	
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
		if (boDTO.getDescription() != null) { ballotOption.setDescription(boDTO.getDescription()); }
		
		ballotOption.setUpdatedDate(new Date());
		
		ballotOptionDao.save(ballotOption);
		
		response.attachGenericSuccess();
		
		return response;
	}
}
