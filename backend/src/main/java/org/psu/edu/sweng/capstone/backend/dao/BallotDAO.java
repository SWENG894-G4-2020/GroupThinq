package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallotDAO extends JpaRepository<Ballot, Long> {}