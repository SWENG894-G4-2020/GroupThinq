package org.psu.edu.sweng.capstone.backend.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotResult;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotResultDAO extends JpaRepository<BallotResult, Long> {
	ArrayList<BallotResult> findAllByBallot(Ballot ballot);

	Optional<BallotResult> findByUserAndBallot(User user, Ballot ballot);
}
