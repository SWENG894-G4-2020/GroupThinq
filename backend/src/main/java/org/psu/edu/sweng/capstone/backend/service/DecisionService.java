package org.psu.edu.sweng.capstone.backend.service;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.UserDTO;

public interface DecisionService {

	List<UserDTO> getUsers(Long id);
}
