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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BALLOT")
public class Ballot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DECISION_ID")
	private Decision decision;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BALLOT_TYPE_ID")
	private BallotType type;
	
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	
	@OneToMany(mappedBy = "ballot", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = BallotOption.class)
	private Set<BallotOption> options = new HashSet<>();
	
	@OneToMany(mappedBy = "ballot", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = BallotVote.class)
	private Set<BallotVote> votes = new HashSet<>();
	
	@OneToMany(mappedBy = "ballot", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = RankedPairWinner.class)
	private Set<RankedPairWinner> rankedPairWinners = new HashSet<>();
	
	protected Ballot() {}
	
	public Ballot(Decision decision, BallotType type, Date expirationDate) {
		this.decision = decision;
		this.type = type;
		this.expirationDate = expirationDate;
		
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

	public BallotType getType() {
		return type;
	}

	public void setType(BallotType type) {
		this.type = type;
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

	public Set<BallotOption> getOptions() {
		return options;
	}

	public void setOptions(Set<BallotOption> ballotOptions) {
		this.options = ballotOptions;
	}

	public Set<BallotVote> getVotes() {
		return votes;
	}

	public void setVotes(Set<BallotVote> results) {
		this.votes = results;
	}

	public Set<RankedPairWinner> getRankedPairWinners() {
		return rankedPairWinners;
	}

	public void setRankedPairWinners(Set<RankedPairWinner> rankedPairWinners) {
		this.rankedPairWinners = rankedPairWinners;
	}
}
