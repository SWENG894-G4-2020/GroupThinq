package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.BallotResultDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.RoleDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserRoleDAO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.exception.EntityConflictException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends ServiceImplTest {
		
	@Mock
	private UserDAO userDao;
	
	@Mock
	private RoleDAO roleDao;
	
	@Mock
	private DecisionDAO decisionDao;
	
	@Mock
	private UserRoleDAO userRoleDao;
	
	@Mock
	private BallotResultDAO ballotResultDao;
	
	@Mock
	private DecisionUserDAO decisionUserDao;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	private String userName;
	private String password; 
	private String lastName;
	private String firstName;
	private String emailAddress;
	private Date birthDate;
	private Date createdDate;
	private Date updatedDate;
	private Date lastLoggedIn;
	
	private User user;
	private UserDTO userDto;
	
	@BeforeEach
	void setUp() {        
        // given
		userName = "JUnitTestUser";
		password = "fakepw";
		lastName = "User";
		firstName = "Test";
		emailAddress = "testUser@foo.bar";
		birthDate = new Date(1337L);
		createdDate = new Date(1111L);
		updatedDate = new Date (2222L);
		lastLoggedIn = new Date(3333L);
		
		user = new User(userName, password, lastName, firstName, emailAddress, birthDate);
		user.setCreatedDate(createdDate);
		user.setUpdatedDate(updatedDate);
		user.setLastLoggedIn(lastLoggedIn);
		
		userDto = UserDTO.build(user);
		userDto.setPassword("fakepw");
	}
		
	@Test
	void createUser_worksProperly_withUserNotAlreadyInSystemAndRoleData() throws EntityConflictException {
		// given
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		when(roleDao.findByName(RoleEnum.USER.getDescription())).thenReturn(Optional.of(new Role()));
		when(bCryptPasswordEncoder.encode(password)).thenReturn("fdsjiaopfjsdaiopfdjdsopifaj");
		ResponseEntity<UserDTO> response = userServiceImpl.createUser(userDto);

		// then
		assertCreatedSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void createUser_worksProperly_withUserNotAlreadyInSystemAndNoRoleData() throws EntityConflictException {
		// given
		Optional<Role> noRole = Optional.empty();
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		when(roleDao.findByName(RoleEnum.USER.getDescription())).thenReturn(noRole);
		when(bCryptPasswordEncoder.encode(password)).thenReturn("fdsjiaopfjsdaiopfdjdsopifaj");
		ResponseEntity<UserDTO> response = userServiceImpl.createUser(userDto);

		// then
		assertCreatedSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void createUser_worksProperly_withUserAlreadyInSystem() throws EntityConflictException {
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
	    assertThrows(EntityConflictException.class, () -> { userServiceImpl.createUser(userDto); });
	}
	
	@Test
	void getUser_returnsUserThatExists() throws EntityNotFoundException {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.getUser(userName);
		
		// then
		assertEquals(userDto.getUserName(), response.getData().get(0).getUserName());
		assertEquals(userDto.getLastName(), response.getData().get(0).getLastName());
		assertEquals(userDto.getFirstName(), response.getData().get(0).getFirstName());
		assertEquals(userDto.getEmailAddress(), response.getData().get(0).getEmailAddress());
	}
	
	@Test
	void getUser_returnsNoUser() throws EntityNotFoundException {
		// given
		Optional<User> noUser = Optional.empty();
		String username = "JUnitTestUser";
		
		// when
		when(userDao.findByUserName(username)).thenReturn(noUser);
		
		// then
	    assertThrows(EntityNotFoundException.class, () -> { userServiceImpl.getUser(username); });
	}
	
	@Test
	void getUsers_returnsListOfUsers() throws Exception {
		// given
		User user1 = new User("mboyer87", "fakepw", "Boyer", "Matt", "mboyer87@gmail.com", new Date(1337L));
		User user2 = new User("testUser", "fakepw", "User", "Test", "testUser@foo.bar", new Date(1337L));
		
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		
		UserDTO userDto1 = UserDTO.build(user1);
		UserDTO userDto2 = UserDTO.build(user2);
		
		List<UserDTO> userDTOList = new ArrayList<>();
		userDTOList.add(userDto1);
		userDTOList.add(userDto2);		
		
		// when
		when(userDao.findAll()).thenReturn(userList);
		
		// then
		assertEquals(2, userServiceImpl.getUsers().getData().size());		
	}
	
	@Test
	void deleteUser_worksProperly_withUserNotAlreadyInSystem() throws EntityNotFoundException {
		// given
		Optional<User> noUser = Optional.empty();		
		String username = "JUnitTestUser";
				
		// when
		when(userDao.findByUserName(username)).thenReturn(noUser);

		// then
	    assertThrows(EntityNotFoundException.class, () -> { userServiceImpl.deleteUser(username); });
		verify(userDao, times(0)).delete(Mockito.any());
	}
	
	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_noChildDependencies() throws EntityNotFoundException {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
		
		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).delete(Mockito.any());
	}

	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_childDependencies() throws EntityNotFoundException {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
		
		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).delete(Mockito.any());
	}
	
	@Test
	void updateUser_savesUser_whenGivenNullValues() throws EntityNotFoundException {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, new UserDTO());

		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());		
	}
	
	@Test
	void updateUser_returnsNull_whenNoUserToUpdate() throws EntityNotFoundException {
		// given
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);

		// then
		assertThrows(EntityNotFoundException.class, () -> { userServiceImpl.updateUser(userName, userDto); });
		verify(userDao, times(0)).save(Mockito.any());	
	}
	
	@Test
	void updateUser_savesUser_whenGivenUserThatExists() throws EntityNotFoundException {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, userDto);

		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void getDecisions_hasNoUser() throws EntityNotFoundException {
	    when(userDao.findByUserName(userName)).thenReturn(Optional.empty());
	    assertThrows(EntityNotFoundException.class, () -> { userServiceImpl.getDecisions(userName); });
	}
	
	@Test
	void getDecisions_hasUser_noDecisionUsers() throws EntityNotFoundException {
		// given
		Optional<User> userOptional = Optional.of(user);

		// when
		when(userDao.findByUserName(userName)).thenReturn(userOptional);
	    ResponseEntity<DecisionDTO> response = userServiceImpl.getDecisions(userName);
		
		// then
	    assertEquals(0, response.getData().size());
	}
	
	@Test
	void getDecisions_hasUser_hasDecision() throws EntityNotFoundException {
		// given
		Decision decisionOne = new Decision("New Decision #1", user);
		Decision decisionTwo  = new Decision("New Decision #2", user);
				
		user.getDecisions().add(new DecisionUser(decisionOne, user));
		user.getDecisions().add(new DecisionUser(decisionTwo, user));
		
		Optional<User> userOptional = Optional.of(user);

		// when
		when(userDao.findByUserName(userName)).thenReturn(userOptional);
		ResponseEntity<DecisionDTO> response = userServiceImpl.getDecisions(userName);
		
		// then
	    assertEquals(2, response.getData().size());
	}
}
