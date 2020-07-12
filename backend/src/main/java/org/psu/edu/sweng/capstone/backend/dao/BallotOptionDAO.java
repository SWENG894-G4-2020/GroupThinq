package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface BallotOptionDAO extends JpaRepository<BallotOption, Long> {
    ArrayList<BallotOption> findAllByOwnerId(User user);
    ArrayList<BallotOption> findAllByBallotId(Ballot ballot);
}
