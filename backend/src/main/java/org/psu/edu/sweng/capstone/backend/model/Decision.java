package org.psu.edu.sweng.capstone.backend.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "DECISION")
public class Decision {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToMany(mappedBy = "decision", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = DecisionUser.class)
	@Fetch(FetchMode.SELECT)
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DecisionUser> getDecisionUsers() {
		return decisionUsers;
	}

	public void setDecisionUsers(Set<DecisionUser> users) {
		this.decisionUsers = users;
	}
}
