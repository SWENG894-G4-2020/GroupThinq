package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionDAO extends JpaRepository<Decision, Long> {
	void deleteByOwnerId(User user);
	
	@Modifying
	@Query("delete from Decision d where d.ownerId = :ownerId")
	void deleteByUser(@Param("ownerId") User ownerId);
}
