package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.RoleDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.exception.EntityConflictException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
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
	
	private static final String ERROR_HEADER = "User ";
	
	@Override
	public ResponseEntity<UserDTO> getUsers() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		List<User> users = userDao.findAll();
		
		users.stream().forEach(u -> response.getData().add(UserDTO.build(u)));
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> getUser(final String username) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		final User user = userDao.findByUserName(username).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + username));
		
		response.getData().add(UserDTO.build(user));
		response.attachGenericSuccess();
			
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> deleteUser(final String username) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		final User user = userDao.findByUserName(username).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + username));
		

		user.getRoles().clear();
		user.getDecisions().clear();
		user.getVotes().clear();

		userDao.delete(user);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> updateUser(final String username, final UserDTO userDto) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final User user = userDao.findByUserName(username).orElseThrow(() -> new EntityNotFoundException(ERROR_HEADER + username));
		
		if (userDto.getLastName() != null) { user.setLastName(userDto.getLastName()); }
		if (userDto.getFirstName() != null) { user.setFirstName(userDto.getFirstName()); }
		if (userDto.getEmailAddress() != null) { user.setEmailAddress(userDto.getEmailAddress()); }
		if (userDto.getBirthDate() != null) { user.setBirthDate(userDto.getBirthDate()); }

		user.setUpdatedDate(new Date());
		
		userDao.save(user);
		
		response.attachGenericSuccess();
				
		return response;
	}

	@Override
	public ResponseEntity<UserDTO> createUser(final UserDTO userDto) throws EntityConflictException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final String username = userDto.getUserName();
		final Optional<User> user = userDao.findByUserName(username);
		
		if (user.isPresent()) {
			throw new EntityConflictException(ERROR_HEADER + user.get().getUserName());
		}
		else {
			createNewUser(userDto);
		}
		
		response.attachCreatedSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecisions(final String username) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final User user = userDao.findByUserName(username).orElseThrow(() -> new EntityNotFoundException(ERROR_HEADER + username));
				
		user.getDecisions().stream().forEach(du -> response.getData().add(DecisionDTO.build(du.getDecision())));
		
		response.attachGenericSuccess();
		
		return response;
	}
	
	private void createNewUser(UserDTO userDto) {
		User newUser = new User(userDto.getUserName(),
				bCryptPasswordEncoder.encode(userDto.getPassword()),
				userDto.getLastName(),
				userDto.getFirstName(),
				userDto.getEmailAddress(),
				userDto.getBirthDate()
		);
		
		newUser.setCreatedDate(new Date());
		
		final Optional<Role> role = roleDao.findByName(RoleEnum.USER.getDescription());
		
		if (role.isPresent()) { newUser.getRoles().add(new UserRole(newUser, role.get())); }
		
		userDao.save(newUser);
	}
}
