package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.RoleDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserRoleDAO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.ResponseError;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;
import org.psu.edu.sweng.capstone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private UserRoleDAO userRoleDao;
	
	@Autowired
	private DecisionUserDAO decisionUserDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@Override
	public ResponseEntity<UserDTO> getUsers() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		try {
			List<User> users = userDao.findAll();
			
			for (User u : users) {
				UserDTO userDto = UserDTO.buildDTO(u);
				response.getData().add(userDto);
			}
			
			response.attachGenericSuccess();
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> getUser(String userName) {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		try {
			Optional<User> user = userDao.findByUserName(userName);
			
			if (user.isPresent()) {
				response.getData().add(UserDTO.buildDTO(user.get()));
				
				response.attachGenericSuccess();
			}
			else {
				response.getErrors().add(new ResponseError
						(ErrorEnum.RESOURCE_CONFLICT, response.writeUserWasNotFoundMessage(userName)));
				
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setSuccess(false);
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> deleteUser(String userName) {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		try {
			Optional<User> user = userDao.findByUserName(userName);

			if (user.isPresent()) {
				User u = user.get();
				
				ArrayList<UserRole> userRoles = userRoleDao.findAllByUser(u);
				ArrayList<DecisionUser> userDecisions = decisionUserDao.findAllByUser(u);
				
				if (!userRoles.isEmpty()) {
					userRoleDao.deleteAll(userRoles);
				}
				
				if (!userDecisions.isEmpty()) {
					decisionUserDao.deleteAll(userDecisions);
				}
				
				userDao.delete(user.get());
				
				response.attachGenericSuccess();
			}
			else {
				response.getErrors().add(new ResponseError
						(ErrorEnum.RESOURCE_CONFLICT, response.writeUserWasNotFoundMessage(userName)));
				
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setSuccess(false);
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);	
		}
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> updateUser(String userName, UserDTO userDto) {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		try {
			Optional<User> user = userDao.findByUserName(userName);
			
			if (user.isPresent()) {
				User u = user.get();
				
				if (userDto.getLastName() != null) { u.setLastName(userDto.getLastName()); }
				if (userDto.getFirstName() != null) { u.setFirstName(userDto.getFirstName()); }
				if (userDto.getEmailAddress() != null) { u.setEmailAddress(userDto.getEmailAddress()); }
				if (userDto.getBirthDate() != null) { u.setBirthDate(userDto.getBirthDate()); }

				u.setUpdatedDate(new Date());
				
				userDao.save(u);
				
				response.attachGenericSuccess();
			}
			else {
				response.getErrors().add(new ResponseError
						(ErrorEnum.RESOURCE_CONFLICT, response.writeUserWasNotFoundMessage(userName)));
				
				response.setStatus(HttpStatus.NO_CONTENT.value());
				response.setSuccess(false);
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
				
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> createUser(String userName, UserDTO userDto) {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		try {
			Optional<User> user = userDao.findByUserName(userName);
			
			if (user.isPresent()) {
				response.getErrors().add(new ResponseError(ErrorEnum.RESOURCE_CONFLICT, userName + " already exists."));
				
				response.setSuccess(false);
				response.setStatus(HttpStatus.CONFLICT.value());
			}
			else {			
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
				
				response.setSuccess(true);
				response.setStatus(HttpStatus.CREATED.value());
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}
}
