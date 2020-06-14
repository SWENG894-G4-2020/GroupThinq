package org.psu.edu.sweng.capstone.backend.service;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;

public interface DecisionService {

	List<UserDTO> getUsers(Long id);
	String updateDecision(Long id, DecisionDTO decision);
	String createDecision(Long id, DecisionDTO decision);
	String deleteDecision(Long id);
	DecisionDTO getDecision(Long id);

}
