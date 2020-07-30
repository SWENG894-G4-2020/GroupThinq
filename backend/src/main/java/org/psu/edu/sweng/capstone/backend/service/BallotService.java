package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;

public interface BallotService {

	ResponseEntity<BallotDTO> createBallot(final BallotDTO ballot) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballot) throws EntityNotFoundException;

	ResponseEntity<String> castVote(List<BallotResultDTO> vote) throws EntityNotFoundException;
	ResponseEntity<String> updateVote(List<BallotResultDTO> vote) throws EntityNotFoundException;

	ResponseEntity<?> retrieveResults(Long ballotId) throws EntityNotFoundException;
}
