package org.psu.edu.sweng.capstone.backend.security;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @InjectMocks
    private WebSecurityConfig webSecurityConfig;
	
}
