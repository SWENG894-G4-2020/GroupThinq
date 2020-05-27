//package org.psu.edu.sweng.capstone.backend.service.impl;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
//import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
//import org.psu.edu.sweng.capstone.backend.model.User;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//	
//	private String userName;
//	private String lastName;
//	private String firstName;
//	private String emailAddress;
//	private User user;
//	private UserDTO userDto;
//
//	@Mock
//	private UserDAO userDao;
//	
//	@InjectMocks
//	private UserServiceImpl userServiceImpl;
//	
//	@BeforeEach
//	void setup() {
//		userName = "JUnitTestUser";
//		lastName = "User";
//		firstName = "Test";
//		emailAddress = "testUser@foo.bar";
//		
//		userDto = new UserDTO(userName, lastName, firstName, emailAddress);
//		user = new User(userName, lastName, firstName, emailAddress);
//	}
//	
//	@Test
//	void createUser_worksProperly_withUserNotAlreadyInSystem() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(null);
//		String returnMessage = userServiceImpl.createUser(userName, userDto);
//		
//		// then
//		assertEquals(userName + " has been created.", returnMessage);
//		verify(userDao, times(1)).save(Mockito.any());
//	}
//	
//	@Test
//	void createUser_worksProperly_withUserAlreadyInSystem() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(user);
//		String returnMessage = userServiceImpl.createUser("JUnitTestUser", userDto);
//		
//		// then
//		assertEquals("User already exists", returnMessage);
//	}
//	
//	@Test
//	void getUser_returnsUserThatExists() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(user);
//		UserDTO actualDTO = userServiceImpl.getUser(userName);
//		
//		// then
//		assertEquals(userDto.getUserName(), actualDTO.getUserName());
//		assertEquals(userDto.getLastName(), actualDTO.getLastName());
//		assertEquals(userDto.getFirstName(), actualDTO.getFirstName());
//		assertEquals(userDto.getEmailAddress(), actualDTO.getEmailAddress());
//	}
//	
//	@Test
//	void getUser_returnsNoUser() {
//		// given
//		String userName = "JUnitTestUser";
//		
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(null);
//		
//		// then
//		assertNull(userServiceImpl.getUser(userName));		
//	}
//	
//	@Test
//	void getUsers_returnsListOfUsers() {
//		// given
//		User user1 = new User("mboyer87", "Boyer", "Matt", "mboyer87@gmail.com");
//		User user2 = new User("testUser", "User", "Test", "testUser@foo.bar");
//		
//		List<User> userList = new ArrayList<>();
//		userList.add(user1);
//		userList.add(user2);
//		
//		UserDTO userDto1 = new UserDTO("mboyer87", "Boyer", "Matt", "mboyer87@gmail.com");
//		UserDTO userDto2 = new UserDTO("testUser", "User", "Test", "testUser@foo.bar");
//		
//		List<UserDTO> userDTOList = new ArrayList<>();
//		userDTOList.add(userDto1);
//		userDTOList.add(userDto2);		
//		
//		// when
//		when(userDao.findAll()).thenReturn(userList);
//		
//		// then
//		assertEquals(2, userServiceImpl.getUsers().size());		
//	}
//	
//	@Test
//	void deleteUser_worksProperly_withUserNotAlreadyInSystem() {
//		// given
//		String userName = "JUnitTestUser";
//				
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(null);
//		String returnMessage = userServiceImpl.deleteUser(userName);
//		
//		// then
//		assertEquals("User does not exist", returnMessage);
//		verify(userDao, times(0)).delete(Mockito.any());
//	}
//	
//	@Test
//	void deleteUser_worksProperly_withUserAlreadyInSystem() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(user);
//		String returnMessage = userServiceImpl.deleteUser(userName);
//		
//		// then
//		assertEquals(userName + " has been deleted.", returnMessage);
//		verify(userDao, times(1)).delete(Mockito.any());
//	}
//	
//	@Test
//	void updateUser_savesUser_whenGivenNullValues() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(user);
//		String returnMessage = userServiceImpl.updateUser(userName, new UserDTO(userName, null, null, null));
//
//		// then
//		assertEquals(userName + " has been updated.", returnMessage);
//		verify(userDao, times(1)).save(Mockito.any());		
//	}
//	
//	@Test
//	void updateUser_returnsNull_whenNoUserToUpdate() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(null);
//		String returnMessage = userServiceImpl.updateUser(userName, userDto);
//		
//		// then
//		assertEquals("User does not exist", returnMessage);
//		verify(userDao, times(0)).save(Mockito.any());	
//	}
//	
//	@Test
//	void updateUser_savesUser_whenGivenUserThatExists() {
//		// when
//		when(userDao.findByUserName(userName)).thenReturn(user);
//		String returnMessage = userServiceImpl.updateUser(userName, userDto);
//
//		// then
//		assertEquals(userName + " has been updated.", returnMessage);
//		verify(userDao, times(1)).save(Mockito.any());
//	}
//}
