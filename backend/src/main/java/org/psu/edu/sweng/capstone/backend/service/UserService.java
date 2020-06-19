package org.psu.edu.sweng.capstone.backend.service;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;

public interface UserService {
	ResponseEntity<UserDTO> getUsers();
	ResponseEntity<UserDTO> getUser(String userName);
	ResponseEntity<UserDTO> deleteUser(String userName);
	ResponseEntity<UserDTO> updateUser(String userName, UserDTO user);
	ResponseEntity<UserDTO> createUser(String userName, UserDTO user);
	List<DecisionDTO> getDecisions(String userName);
}
