package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityConflictException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;

public interface UserService {
	ResponseEntity<UserDTO> getUsers();
	ResponseEntity<UserDTO> getUser(final String userName) throws EntityNotFoundException;
	ResponseEntity<UserDTO> deleteUser(final String userName) throws EntityNotFoundException;
	ResponseEntity<UserDTO> updateUser(final String userName, UserDTO user) throws EntityNotFoundException;
	ResponseEntity<UserDTO> createUser(final UserDTO user) throws EntityConflictException;
	
	ResponseEntity<DecisionDTO> getDecisions(final String userName) throws EntityNotFoundException;
}
