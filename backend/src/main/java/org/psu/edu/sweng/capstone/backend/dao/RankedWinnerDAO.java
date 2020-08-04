package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RankedWinnerDAO extends JpaRepository<RankedWinner, Long> {
	Optional<RankedWinner> findByBallot(Ballot ballot);
	
	@Modifying
	@Query("delete from RankedWinner rw where rw.ballot = :ballot")
	void deleteByBallot(@Param("ballot") Ballot ballot);
}
