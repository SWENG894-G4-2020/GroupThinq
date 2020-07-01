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
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.exception.EntityConflictException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
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
	private UserRoleDAO userRoleDao;
	
	@Autowired
	private DecisionUserDAO decisionUserDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private static final String ERROR_HEADER = "User ";
	
	@Override
	public ResponseEntity<UserDTO> getUsers() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		List<User> users = userDao.findAll();
		
		for (User u : users) {
			UserDTO userDto = UserDTO.build(u);
			response.getData().add(userDto);
		}
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> getUser(final String username) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		final Optional<User> user = userDao.findByUserName(username);
		
		if (user.isPresent()) {
			response.getData().add(UserDTO.build(user.get()));
			response.attachGenericSuccess();
		}
		else {
			throw new EntityNotFoundException(ERROR_HEADER + username);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> deleteUser(final String username) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		final Optional<User> user = userDao.findByUserName(username);

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
			throw new EntityNotFoundException(ERROR_HEADER + username);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> updateUser(final String username, final UserDTO userDto) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final Optional<User> user = userDao.findByUserName(username);
			
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
			throw new EntityNotFoundException(ERROR_HEADER + username);
		}
				
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> createUser(final UserDTO userDto) throws EntityConflictException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final String username = userDto.getUserName();

		Optional<User> user = userDao.findByUserName(username);
		
		if (user.isPresent()) {
			throw new EntityConflictException(ERROR_HEADER + user.get().getUserName());
		}
		else {
			User newUser = new User(username,
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
			
			response.attachCreatedSuccess();
		}
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecisions(final String username) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Optional<User> user = userDao.findByUserName(username);
		
		if (user.isPresent()) {
			ArrayList<DecisionUser> decisionUsers = decisionUserDao.findAllByUser(user.get());
			
			for (DecisionUser du : decisionUsers) {
				response.getData().add(DecisionDTO.build(du.getDecision()));
			}
			
			response.attachGenericSuccess();
		}
		else {
			throw new EntityNotFoundException(ERROR_HEADER + username);
		}
		
		return response;
	}
}
