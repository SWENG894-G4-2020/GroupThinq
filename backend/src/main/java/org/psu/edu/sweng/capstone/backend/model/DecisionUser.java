package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.psu.edu.sweng.capstone.backend.model.id.DecisionUserId;

@Entity
@Table(name = "DECISION_USERS")
@IdClass(DecisionUserId.class)
public class DecisionUser {
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DECISION_ID")
	private Decision decision;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private User user;

	protected DecisionUser() {}
	
	public DecisionUser(Decision decision, User user) {
		this.decision = decision;
		this.user = user;
	}

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
