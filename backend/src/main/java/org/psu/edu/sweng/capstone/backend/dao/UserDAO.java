package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
	Optional<User> findByUserName(String userName);
}
