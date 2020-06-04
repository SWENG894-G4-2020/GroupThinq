package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
}
