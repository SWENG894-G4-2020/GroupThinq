package org.psu.edu.sweng.capstone.backend.dao;

import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankedWinnerDAO extends JpaRepository<RankedWinner, Long> {
	Optional<RankedWinner> findByBallot(Ballot ballot);
}
