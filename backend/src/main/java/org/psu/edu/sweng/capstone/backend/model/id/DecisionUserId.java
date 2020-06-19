package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;
import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getDecision(), this.getUser());
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (!(o instanceof DecisionUserId)) {
        	return false;
        }
        
        DecisionUserId decisionUserId = (DecisionUserId) o;
        
        return (Objects.equals(this.getDecision(), decisionUserId.getDecision()) &&
        		Objects.equals(this.getUser(), decisionUserId.getUser()));
	}
}
