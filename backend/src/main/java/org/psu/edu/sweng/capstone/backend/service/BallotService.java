package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;

public interface BallotService {

	ResponseEntity<BallotDTO> createBallot(final BallotDTO ballot) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> deleteBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> retrieveBallot(final Long ballotId) throws EntityNotFoundException;
	ResponseEntity<BallotDTO> updateBallot(final Long ballotId, final BallotDTO ballot) throws EntityNotFoundException;

	ResponseEntity<String> castVote(final List<BallotResultDTO> vote) throws EntityNotFoundException;
	ResponseEntity<String> updateVote(final List<BallotResultDTO> vote) throws EntityNotFoundException;

	ResponseEntity<BallotResultDTO> retrieveAllVotes(final Ballot ballot) throws EntityNotFoundException;
	void retrieveRankedChoiceResults(final Ballot ballotId) throws EntityNotFoundException;
}
