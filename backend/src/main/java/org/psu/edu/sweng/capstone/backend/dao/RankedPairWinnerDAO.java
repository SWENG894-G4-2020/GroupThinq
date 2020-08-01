package org.psu.edu.sweng.capstone.backend.dao;

import java.util.ArrayList;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankedPairWinnerDAO extends JpaRepository<RankedPairWinner, Long> {
	ArrayList<RankedPairWinner> findAllByBallotOrderByMarginDesc(Ballot ballot);
}
