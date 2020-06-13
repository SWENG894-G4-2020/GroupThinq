package org.psu.edu.sweng.capstone.backend.security;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
    private UserDAO userDao;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	private final String username = "testuser";
	private final User user = new User("testuser", "fakepw", "User", "Test", "testuser@foo.bar", new Date());
	
	@Test
	void loadUserByUsername_findsAndReturnsUser() {
		when(userDao.findByUserName(username)).thenReturn(Optional.of(user));
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
		
		assertNotNull(userDetails);
	}
	
	@Test
	void loadUserByUsername_findsNoUser() {
		when(userDao.findByUserName(username)).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));
	}
}
