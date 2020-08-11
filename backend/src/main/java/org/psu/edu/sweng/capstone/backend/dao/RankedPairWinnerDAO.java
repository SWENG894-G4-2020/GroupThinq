package org.psu.edu.sweng.capstone.backend.dao;

import java.util.ArrayList;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RankedPairWinnerDAO extends JpaRepository<RankedPairWinner, Long> {
	ArrayList<RankedPairWinner> findAllByBallotOrderByMarginDesc(Ballot ballot);
	
	@Modifying
	@Query("delete from RankedPairWinner rpw where rpw.ballot = :ballot")
	void deleteByBallot(@Param("ballot") Ballot ballot);
}
