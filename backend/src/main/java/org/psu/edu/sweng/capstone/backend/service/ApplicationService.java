package org.psu.edu.sweng.capstone.backend.service;

import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.User;

public interface ApplicationService {
	UserDTO buildUserDTO(User u);
}
