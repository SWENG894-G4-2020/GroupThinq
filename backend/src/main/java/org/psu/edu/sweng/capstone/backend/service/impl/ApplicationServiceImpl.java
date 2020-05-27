package org.psu.edu.sweng.capstone.backend.service.impl;

import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.ApplicationService;

public class ApplicationServiceImpl implements ApplicationService {
	
	@Override
	public UserDTO buildUserDTO(User u) {
		return new UserDTO(u.getUserName(), u.getLastName(), u.getFirstName(), u.getEmailAddress());
	}
}
