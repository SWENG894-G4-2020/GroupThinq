package org.psu.edu.sweng.capstone.backend.dao;

import java.util.ArrayList;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotResult;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotResultDAO extends JpaRepository<BallotResult, Long> {
	ArrayList<BallotResult> findAllByBallot(Ballot ballot);
	
	@Modifying
	@Query("delete from BallotResult br where br.user = :user")
	void deleteByUser(@Param("user") User user);
	
	@Modifying
	@Query("delete from BallotResult br where br.ballot = :ballot")
	void deleteByBallot(@Param("ballot") Ballot ballot);
}
