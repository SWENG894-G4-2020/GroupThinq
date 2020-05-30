package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {}
