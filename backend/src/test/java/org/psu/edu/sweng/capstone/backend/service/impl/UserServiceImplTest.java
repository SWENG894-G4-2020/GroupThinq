package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.Assert.assertNull;
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
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
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
	void createUser_worksProperly_withUserNotAlreadyInSystem() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(null);
		when(roleDao.findByName(RoleEnum.USER.getDescription())).thenReturn(Optional.of(new Role()));
		when(bCryptPasswordEncoder.encode(password)).thenReturn("fdsjiaopfjsdaiopfdjdsopifaj");
		String returnMessage = userServiceImpl.createUser(userName, userDto);

		// then
		assertEquals(userName + " has been created.", returnMessage);
		verify(userDao, times(1)).save(Mockito.any());
	}
	
	@Test
	void createUser_worksProperly_withUserAlreadyInSystem() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		String returnMessage = userServiceImpl.createUser("JUnitTestUser", userDto);
		
		// then
		assertEquals("User already exists", returnMessage);
	}
	
	@Test
	void getUser_returnsUserThatExists() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		UserDTO actualDTO = userServiceImpl.getUser(userName);
		
		// then
		assertEquals(userDto.getUserName(), actualDTO.getUserName());
		assertEquals(userDto.getLastName(), actualDTO.getLastName());
		assertEquals(userDto.getFirstName(), actualDTO.getFirstName());
		assertEquals(userDto.getEmailAddress(), actualDTO.getEmailAddress());
	}
	
	@Test
	void getUser_returnsNoUser() {
		// given
		String userName = "JUnitTestUser";
		
		// when
		when(userDao.findByUserName(userName)).thenReturn(null);
		
		// then
		assertNull(userServiceImpl.getUser(userName));		
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
		assertEquals(2, userServiceImpl.getUsers().size());		
	}
	
	@Test
	void deleteUser_worksProperly_withUserNotAlreadyInSystem() {
		// given
		String userName = "JUnitTestUser";
				
		// when
		when(userDao.findByUserName(userName)).thenReturn(null);
		String returnMessage = userServiceImpl.deleteUser(userName);
		
		// then
		assertEquals("User does not exist", returnMessage);
		verify(userDao, times(0)).delete(Mockito.any());
	}
	
	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_noChildDependencies() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		when(userRoleDao.findAllByUser(user)).thenReturn(new ArrayList<>());
		when(decisionUserDao.findAllByUser(user)).thenReturn(new ArrayList<>());
		String returnMessage = userServiceImpl.deleteUser(userName);
		
		// then
		assertEquals(userName + " has been deleted.", returnMessage);
		verify(userDao, times(1)).delete(Mockito.any());
	}

	@Test
	void deleteUser_worksProperly_withUserAlreadyInSystem_childDependencies() {
		// given
		ArrayList<UserRole> userRoles = new ArrayList<>();
		ArrayList<DecisionUser> decisionUsers = new ArrayList<>();
		
		Decision newDecision = new Decision(2L, "New Decision", new Date());
		User newUser = new User("TReyob", "fakepw", "Reyob", "Ttam", "TtamReyob@gmail.com", new Date(1337L));
		DecisionUser newDecisionUser = new DecisionUser(newDecision, newUser);
		
		// given
		User newUser2 = new User("TReyob", "fakepw", "Reyob", "Ttam", "TtamReyob@gmail.com", new Date(1337L));
		Role newRole = new Role();
		UserRole userRole = new UserRole(newUser2, newRole);
		
		decisionUsers.add(newDecisionUser);
		userRoles.add(userRole);
		
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		when(userRoleDao.findAllByUser(user)).thenReturn(userRoles);
		when(decisionUserDao.findAllByUser(user)).thenReturn(decisionUsers);
		String returnMessage = userServiceImpl.deleteUser(userName);
		
		// then
		assertEquals(userName + " has been deleted.", returnMessage);
		verify(userDao, times(1)).delete(Mockito.any());
	}
	
	@Test
	void updateUser_savesUser_whenGivenNullValues() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		String returnMessage = userServiceImpl.updateUser(userName, new UserDTO());

		// then
		assertEquals(userName + " has been updated.", returnMessage);
		verify(userDao, times(1)).save(Mockito.any());		
	}
	
	@Test
	void updateUser_returnsNull_whenNoUserToUpdate() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(null);
		String returnMessage = userServiceImpl.updateUser(userName, userDto);
		
		// then
		assertEquals("User does not exist", returnMessage);
		verify(userDao, times(0)).save(Mockito.any());	
	}
	
	@Test
	void updateUser_savesUser_whenGivenUserThatExists() {
		// when
		when(userDao.findByUserName(userName)).thenReturn(user);
		String returnMessage = userServiceImpl.updateUser(userName, userDto);

		// then
		assertEquals(userName + " has been updated.", returnMessage);
		verify(userDao, times(1)).save(Mockito.any());
	}
}
