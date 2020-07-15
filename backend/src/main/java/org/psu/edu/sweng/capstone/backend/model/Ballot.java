package org.psu.edu.sweng.capstone.backend.model;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "BALLOT")
@Access(AccessType.FIELD)
public class Ballot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DECISION_ID")
	private Decision decision;
	
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@OneToMany(mappedBy = "ballot", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = BallotOption.class)
	private Set<BallotOption> ballotOptions = new HashSet<>();

	@Column(name = "VOTES")
	private HashMap<String, LinkedList<Long>> ballotVotes = new HashMap<>();

	protected Ballot() {}
	
	public Ballot(Decision decision, Date expirationDate, Set<BallotOption> ballotOptions) {
		this.decision = decision;
		this.expirationDate = expirationDate;
		if(ballotOptions != null && ballotOptions.size() > 0) {
			this.ballotOptions = ballotOptions;
		}

		this.setCreatedDate(new Date());
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Set<BallotOption> getBallotOptions() {
		return ballotOptions;
	}

	public void setBallotOptions(Set<BallotOption> ballotOptions) {
		this.ballotOptions = ballotOptions;
	}

	public HashMap<String, LinkedList<Long>> getBallotVotes() {
		return ballotVotes;
	}

	public void setBallotVotes(HashMap<String, LinkedList<Long>> ballotVotes) {
		this.ballotVotes = ballotVotes;
	}
}
