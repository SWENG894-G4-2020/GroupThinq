package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;

public interface BallotService {

	ResponseEntity<BallotDTO> createBallot(BallotDTO ballot);
	ResponseEntity<BallotDTO> deleteBallot(Long ballotId);
	ResponseEntity<BallotDTO> retrieveBallot(Long ballotId);
	ResponseEntity<BallotDTO> updateBallot(Long ballotId, BallotDTO ballot);
}
