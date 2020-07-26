package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;

public interface DecisionService {

	ResponseEntity<DecisionDTO> getDecisions();
	
	ResponseEntity<DecisionDTO> updateDecision(final Long id, final DecisionDTO decision) throws EntityNotFoundException;
	ResponseEntity<DecisionDTO> createDecision(final DecisionDTO decision) throws EntityNotFoundException;
	ResponseEntity<DecisionDTO> deleteDecision(final Long id) throws EntityNotFoundException;
	ResponseEntity<DecisionDTO> getDecision(final Long id) throws EntityNotFoundException;
	
	ResponseEntity<UserDTO> getUsers(final Long id) throws EntityNotFoundException;
}
