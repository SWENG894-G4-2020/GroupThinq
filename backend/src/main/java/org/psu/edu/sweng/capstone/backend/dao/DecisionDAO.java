package org.psu.edu.sweng.capstone.backend.dao;

import java.util.ArrayList;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DecisionDAO extends JpaRepository<Decision, Long> {
    Decision findByName(String decisionName);
    ArrayList<Decision> findAllByOwnerId(User user);
}
