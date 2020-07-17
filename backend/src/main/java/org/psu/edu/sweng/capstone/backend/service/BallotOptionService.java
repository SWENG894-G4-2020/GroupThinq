package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;

public interface BallotOptionService {
	ResponseEntity<BallotOptionDTO> updateBallotOption(final Long ballotOptionId, final BallotOptionDTO ballotOption) throws EntityNotFoundException;
}
