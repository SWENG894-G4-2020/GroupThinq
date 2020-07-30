package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotVote;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotVoteDAO extends JpaRepository<BallotVote, Long> {
	Optional<BallotVote> findByUserAndBallot(User user, Ballot ballot);
	
	@Modifying
	@Query("delete from BallotVote bv where bv.user = :user")
	void deleteByUser(@Param("user") User user);
	
	@Modifying
	@Query("delete from BallotVote bv where bv.ballot = :ballot")
	void deleteByBallot(@Param("ballot") Ballot ballot);
}
