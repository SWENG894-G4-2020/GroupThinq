package org.psu.edu.sweng.capstone.backend.security;

import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userDao.findByUserName(username);
        
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        
        User u = user.get();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(UserRole role: u.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
        }
        return new org.springframework.security.core.userdetails.User(u.getUserName(), u.getPassword(), grantedAuthorities);
    }
}
