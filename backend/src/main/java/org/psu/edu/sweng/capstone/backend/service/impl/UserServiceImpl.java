package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.impl.UserService;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDao;
	
	@Override
	public List<UserDTO> getUsers() {
		List<User> users = userDao.findAll();
		
		List<UserDTO> response = new ArrayList<>();
		for (User u : users) {
			UserDTO userDto = buildUserDTO(u);
			response.add(userDto);
		}
		
		return response;
	}

	@Override
	public UserDTO getUser(String userName) {
		User user = userDao.findByUserName(userName);
		
		return buildUserDTO(user);
	}
	
	@Override
	public String deleteUser(String userName) {
		User user = userDao.findByUserName(userName);
		
		if (user == null) {
			return "User does not exist";
		}

		userDao.delete(user);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been deleted.");
		
		return builder.toString();
	}

	@Override
	public String updateUser(String userName, String lastName, String firstName, String email) {
		User user = userDao.findByUserName(userName);
		
		if (user == null) {
			return "User does not exist";
		}
		
		if (lastName != null) {
			user.setLastName(lastName);
		}
		
		if (firstName != null) {
			user.setFirstName(firstName);
		}
		
		if (email != null) {
			user.setEmailAddress(email);
		}
		
		userDao.save(user);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been updated.");
		
		return builder.toString();
	}

	@Override
	public String createUser(String userName, String lastName, String firstName, String email) {
		User user = userDao.findByUserName(userName);
		
		if (user != null) {
			return "User already exists";
		}
		
		User newUser = new User(userName, lastName, firstName, email);
		userDao.save(newUser);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been created.");
		
		return builder.toString();
	}
	
	private UserDTO buildUserDTO(User u) {
		UserDTO userDto = new UserDTO();
		
		userDto.setFirstName(u.getFirstName());
		userDto.setLastName(u.getLastName());
		userDto.setEmailAddress(u.getEmailAddress());
		
		return userDto;
	}
}
