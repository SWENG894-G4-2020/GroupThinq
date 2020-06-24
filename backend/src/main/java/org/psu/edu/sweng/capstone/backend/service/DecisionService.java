package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;

public interface DecisionService {

	ResponseEntity<DecisionDTO> updateDecision(Long id, DecisionDTO decision);
	ResponseEntity<DecisionDTO> createDecision(DecisionDTO decision);
	ResponseEntity<DecisionDTO> deleteDecision(Long id);
	ResponseEntity<DecisionDTO> getDecision(Long id);
	
	ResponseEntity<UserDTO> getUsers(Long id);
}
