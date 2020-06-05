package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.RoleDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;
import org.psu.edu.sweng.capstone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public List<UserDTO> getUsers() {
		List<User> users = userDao.findAll();
		
		List<UserDTO> response = new ArrayList<>();
		for (User u : users) {
			UserDTO userDto = UserDTO.buildDTO(u);
			response.add(userDto);
		}
		
		return response;
	}

	@Override
	public UserDTO getUser(String userName) {
		User user = userDao.findByUserName(userName);
		
		return (user != null) ? UserDTO.buildDTO(user) : null;
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
		
		if (userDto.getLastName() != null) { user.setLastName(userDto.getLastName()); }
		if (userDto.getFirstName() != null) { user.setFirstName(userDto.getFirstName()); }
		if (userDto.getEmailAddress() != null) { user.setEmailAddress(userDto.getEmailAddress()); }
		if (userDto.getBirthDate() != null) { user.setBirthDate(userDto.getBirthDate()); }

		user.setUpdatedDate(new Date());
		
		userDao.save(user);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been updated.");
		
		return builder.toString();
	}

	@Override
	public String createUser(String userName, UserDTO userDto) {
		User user = userDao.findByUserName(userName);
		
		if (user != null) { return "User already exists"; }
		
		User newUser = new User(userName,
				bCryptPasswordEncoder.encode(userDto.getPassword()),
				userDto.getLastName(),
				userDto.getFirstName(),
				userDto.getEmailAddress(),
				userDto.getBirthDate()
		);
		
		newUser.setCreatedDate(new Date());
		
		Optional<Role> role = roleDao.findByName(RoleEnum.USER.getDescription());
		
		if (role.isPresent()) {
			newUser.getUserRoles().add(new UserRole(newUser, role.get()));
		}
		
		userDao.save(newUser);
		
		StringBuilder builder = new StringBuilder();
		builder.append(userName).append(" has been created.");
		
		return builder.toString();
	}
}
