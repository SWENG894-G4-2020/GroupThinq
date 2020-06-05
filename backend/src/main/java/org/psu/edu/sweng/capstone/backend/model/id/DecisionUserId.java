package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;

public class DecisionUserId implements Serializable {
	private static final long serialVersionUID = 1689625905121985251L;
	
	private Long decision;
	private Long user;

	public DecisionUserId() {}
	
	public DecisionUserId(Long decision, Long user) {
		this.decision = decision;
		this.user = user;
	}

	public Long getDecision() {
		return decision;
	}

	public Long getUser() {
		return user;
	}
}
