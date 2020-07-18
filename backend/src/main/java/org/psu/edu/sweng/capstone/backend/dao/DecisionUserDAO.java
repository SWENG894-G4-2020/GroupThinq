package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionUserDAO extends JpaRepository<DecisionUser, Long> {
	Optional<DecisionUser> findByUserAndDecision(User user, Decision decision);

	@Modifying
	@Query("delete from DecisionUser du where du.user = :user and du.decision = :decision")
	void deleteByUserAndDecision(@Param("user") User user, @Param("decision") Decision decision);
}
