package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface BallotOptionDAO extends JpaRepository<BallotOption, Long> {
    ArrayList<BallotOption> findAllByuser(final User user);
    ArrayList<BallotOption> findAllByBallot(final Ballot ballot);
}
