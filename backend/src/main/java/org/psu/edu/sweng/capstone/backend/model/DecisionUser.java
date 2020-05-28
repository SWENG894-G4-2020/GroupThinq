package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DECISION_USERS")
public class DecisionUser {
	
	@Id
	@Column
	private Long id;
	
	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "DECISION_ID")
	private Decision decision;
	
	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name = "USERNAME")
	private User user;

	protected DecisionUser() {}
	
	public DecisionUser(Long id, Decision decision, User user) {
		this.id = id;
		this.decision = decision;
		this.user = user;
	}
	
	public Long getId() {
		return id;
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
