package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.RoleDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserRoleDAO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	
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

	@Mock
	private UserDAO userDao;
	
	@Mock
	private RoleDAO roleDao;
	
	@Mock
	private UserRoleDAO userRoleDao;
	
	@Mock
	private DecisionUserDAO decisionUserDao;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@BeforeEach
	void setup() {
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
		
		userDto = UserDTO.buildDTO(user);
		userDto.setPassword("fakepw");
	}

	@Test
	void createUser_worksProperly_withUserNotAlreadyInSystemAndRoleData() {
		// given
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		when(roleDao.findByName(RoleEnum.USER.getDescription())).thenReturn(Optional.of(new Role()));
		when(bCryptPasswordEncoder.encode(password)).thenReturn("fdsjiaopfjsdaiopfdjdsopifaj");
		ResponseEntity<UserDTO> response = userServiceImpl.createUser(userName, userDto);

		// then
		assertEquals(true, response.getSuccess());
		assertEquals(201, response.getStatus());
		assertEquals(0, response.getErrors().size());
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void createUser_worksProperly_withUserNotAlreadyInSystemAndNoRoleData() {
		// given
		Optional<Role> noRole = Optional.empty();
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		when(roleDao.findByName(RoleEnum.USER.getDescription())).thenReturn(noRole);
		when(bCryptPasswordEncoder.encode(password)).thenReturn("fdsjiaopfjsdaiopfdjdsopifaj");
		ResponseEntity<UserDTO> response = userServiceImpl.createUser(userName, userDto);

		// then
		assertEquals(true, response.getSuccess());
		assertEquals(201, response.getStatus());
		assertEquals(0, response.getErrors().size());
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void createUser_worksProperly_withUserAlreadyInSystem() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.createUser("JUnitTestUser", userDto);
		
		// then
		assertEquals(1, response.getErrors().size());
		assertEquals(409, response.getStatus());
		assertEquals(false, response.getSuccess());
	}
	
	@Test
	void getUser_returnsUserThatExists() {
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
	void getUser_returnsNoUser() {
		// given
		Optional<User> noUser = Optional.empty();
		String userName = "JUnitTestUser";
		
		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		ResponseEntity<UserDTO> response = userServiceImpl.getUser(userName);
		
		// then
		assertResourceConflictIssues(response);
		assertEquals(0, response.getData().size());		
	}
	
	@Test
	void getUsers_returnsListOfUsers() {
		// given
		User user1 = new User("mboyer87", "fakepw", "Boyer", "Matt", "mboyer87@gmail.com", new Date(1337L));
		User user2 = new User("testUser", "fakepw", "User", "Test", "testUser@foo.bar", new Date(1337L));
		
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		
		UserDTO userDto1 = UserDTO.buildDTO(user1);
		UserDTO userDto2 = UserDTO.buildDTO(user2);
		
		List<UserDTO> userDTOList = new ArrayList<>();
		userDTOList.add(userDto1);
		userDTOList.add(userDto2);		
		
		// when
		when(userDao.findAll()).thenReturn(userList);
		
		// then
		assertEquals(2, userServiceImpl.getUsers().getData().size());		
	}
	
	@Test
	void deleteUser_worksProperly_withUserNotAlreadyInSystem() {
		// given
		Optional<User> noUser = Optional.empty();
		
		String userName = "JUnitTestUser";
				
		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
		
		// then
		assertResourceConflictIssues(response);
		verify(userDao, times(0)).delete(Mockito.any());
	}
	
	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_noChildDependencies() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		when(userRoleDao.findAllByUser(user)).thenReturn(new ArrayList<>());
		when(decisionUserDao.findAllByUser(user)).thenReturn(new ArrayList<>());
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
		
		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).delete(Mockito.any());
	}

	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_childDependencies() {
		// given
		ArrayList<UserRole> userRoles = new ArrayList<>();
		ArrayList<DecisionUser> decisionUsers = new ArrayList<>();
		
		Decision newDecision = new Decision(2L, "New Decision");
		User newUser = new User("TReyob", "fakepw", "Reyob", "Ttam", "TtamReyob@gmail.com", new Date(1337L));
		DecisionUser newDecisionUser = new DecisionUser(newDecision, newUser);
		
		// given
		User newUser2 = new User("TReyob", "fakepw", "Reyob", "Ttam", "TtamReyob@gmail.com", new Date(1337L));
		Role newRole = new Role();
		UserRole userRole = new UserRole(newUser2, newRole);
		
		decisionUsers.add(newDecisionUser);
		userRoles.add(userRole);
		
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		when(userRoleDao.findAllByUser(user)).thenReturn(userRoles);
		when(decisionUserDao.findAllByUser(user)).thenReturn(decisionUsers);
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
		
		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).delete(Mockito.any());
	}
	
	@Test
	void updateUser_savesUser_whenGivenNullValues() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, new UserDTO());

		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());		
	}
	
	@Test
	void updateUser_returnsNull_whenNoUserToUpdate() {
		// given
		Optional<User> noUser = Optional.empty();

		// when
		when(userDao.findByUserName(userName)).thenReturn(noUser);
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, userDto);
		
		// then
		assertResourceConflictIssues(response);
		verify(userDao, times(0)).save(Mockito.any());	
	}
	
	@Test
	void updateUser_savesUser_whenGivenUserThatExists() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(Optional.of(user));
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, userDto);

		// then
		assertGenericSuccess(response);
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	public void getUsers_handlesExceptionProperly() {
	    when(userDao.findAll()).thenThrow(RuntimeException.class);
		ResponseEntity<UserDTO> response = userServiceImpl.getUsers();
	    
		assertExceptionThrown(response);
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	@Test
	public void getUser_handlesExceptionProperly() {
	    when(userDao.findByUserName(userName)).thenThrow(RuntimeException.class);
		ResponseEntity<UserDTO> response = userServiceImpl.getUser(userName);
	    
		assertExceptionThrown(response);
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	@Test
	public void deleteUser_handlesExceptionProperly() {
	    when(userDao.findByUserName(userName)).thenThrow(RuntimeException.class);
		ResponseEntity<UserDTO> response = userServiceImpl.deleteUser(userName);
	    
		assertExceptionThrown(response);
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	@Test
	public void updateUser_handlesExceptionProperly() {
	    when(userDao.findByUserName(userName)).thenThrow(RuntimeException.class);
		ResponseEntity<UserDTO> response = userServiceImpl.updateUser(userName, userDto);
	    
		assertExceptionThrown(response);
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	@Test
	public void createUser_handlesExceptionProperly() {
	    when(userDao.findByUserName(userName)).thenThrow(RuntimeException.class);
		ResponseEntity<UserDTO> response = userServiceImpl.createUser(userName, userDto);
	    
		assertExceptionThrown(response);
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	private void assertExceptionThrown(ResponseEntity<UserDTO> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(500, response.getStatus());
		assertEquals(false, response.getSuccess());
	}

	private void assertGenericSuccess(ResponseEntity<UserDTO> response) {
		assertEquals(true, response.getSuccess());
		assertEquals(200, response.getStatus());
		assertEquals(0, response.getErrors().size());
	}
	
	private void assertResourceConflictIssues(ResponseEntity<UserDTO> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(204, response.getStatus());
		assertEquals(false, response.getSuccess());
	}
}
