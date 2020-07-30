package org.psu.edu.sweng.capstone.backend.dao;

import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankedWinnerDAO extends JpaRepository<RankedWinner, Long> {}
