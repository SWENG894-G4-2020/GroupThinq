package org.psu.edu.sweng.capstone.backend.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DECISION")
public class Decision {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_ID")
	private User ownerId;

	@OneToMany(mappedBy = "decision", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = DecisionUser.class)
	private Set<DecisionUser> decisionUsers = new HashSet<>();
	
	@OneToMany(mappedBy = "decision", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Ballot.class)
	private Set<Ballot> ballots = new HashSet<>();

	protected Decision() {}

	public Decision(String name, String description, User ownerId) {
		this.name = name;
		this.description = description;
		this.ownerId = ownerId;
		
		this.setCreatedDate(new Date());
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Set<DecisionUser> getDecisionUsers() {
		return decisionUsers;
	}

	public void setDecisionUsers(Set<DecisionUser> users) {
		this.decisionUsers = users;
	}

	public Set<Ballot> getBallots() {
		return ballots;
	}

	public void setBallots(Set<Ballot> ballots) {
		this.ballots = ballots;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
