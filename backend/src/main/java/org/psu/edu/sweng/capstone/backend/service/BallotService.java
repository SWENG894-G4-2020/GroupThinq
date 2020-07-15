package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.BallotExpiredException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;

import java.util.List;

public interface BallotService {

	ResponseEntity<BallotDTO> createBallot(final BallotDTO ballot) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballot) throws EntityNotFoundException;
	ResponseEntity<BallotOptionDTO> retrieveBallotOptions(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotOptionDTO> addBallotOptions(final Long ballotId, final List<BallotOptionDTO> ballotOptions) throws EntityNotFoundException, BallotExpiredException;
	ResponseEntity<BallotDTO> addVote(final Long ballotId, final Long ballotOptionId, final String userName) throws EntityNotFoundException, BallotExpiredException;

	}
