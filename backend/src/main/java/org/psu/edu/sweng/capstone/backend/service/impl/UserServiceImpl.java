package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.UserService;
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
		
		return (user != null) ? buildUserDTO(user) : null;
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
	public String updateUser(String userName, UserDTO userDto) {
		User user = userDao.findByUserName(userName);
		
		if (user == null) {
			return "User does not exist";
		}
		
		if (userDto.getLastName() != null) {
			user.setLastName(userDto.getLastName());
		}
		
		if (userDto.getFirstName() != null) {
			user.setFirstName(userDto.getFirstName());
		}
		
		if (userDto.getEmailAddress() != null) {
			user.setEmailAddress(userDto.getEmailAddress());
		}
		
		userDao.save(user);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been updated.");
		
		return builder.toString();
	}

	@Override
	public String createUser(String userName, UserDTO userDto) {
		User user = userDao.findByUserName(userName);
		
		if (user != null) {
			return "User already exists";
		}
		
		User newUser = new User(userName, userDto.getLastName(), userDto.getFirstName(), userDto.getEmailAddress());
		userDao.save(newUser);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been created.");
		
		return builder.toString();
	}
	
	private UserDTO buildUserDTO(User u) {
		return new UserDTO(u.getUserName(), u.getLastName(), u.getFirstName(), u.getEmailAddress());
	}
}
