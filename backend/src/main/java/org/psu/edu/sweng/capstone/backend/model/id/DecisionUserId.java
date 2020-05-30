package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.User;

public class DecisionUserId implements Serializable {
	private static final long serialVersionUID = 1689625905121985251L;
	
	private Decision decision;
	private User user;

	public DecisionUserId() {}
	
	public DecisionUserId(Decision decision, User user) {
		this.decision = decision;
		this.user = user;
	}

	public Decision getDecision() {
		return decision;
	}

	public User getUser() {
		return user;
	}
}
