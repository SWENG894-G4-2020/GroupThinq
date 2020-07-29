package org.psu.edu.sweng.capstone.backend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BALLOT_RESULT")
public class BallotResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BALLOT_ID")
	private Ballot ballot;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BALLOT_OPTION_ID")
	private BallotOption ballotOption;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@Column(name = "RANK")
	private Long rank;
	
	@Column(name = "VOTE_DATE")
	private Date voteDate;
	
	@Column(name = "VOTE_UPDATED_DATE")
	private Date voteUpdatedDate;
	
	protected BallotResult() {}
	
	public BallotResult(Ballot ballot, BallotOption ballotOption, User user) {
		this.ballot = ballot;
		this.ballotOption = ballotOption;
		this.user = user;
		
		this.voteDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ballot getBallot() {
		return ballot;
	}

	public void setBallot(Ballot ballot) {
		this.ballot = ballot;
	}

	public BallotOption getBallotOption() {
		return ballotOption;
	}

	public void setBallotOption(BallotOption ballotOption) {
		this.ballotOption = ballotOption;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Date getVoteDate() {
		return voteDate;
	}

	public void setVoteDate(Date voteDate) {
		this.voteDate = voteDate;
	}

	public Date getVoteUpdatedDate() {
		return voteUpdatedDate;
	}

	public void setVoteUpdatedDate(Date voteUpdatedDate) {
		this.voteUpdatedDate = voteUpdatedDate;
	}
}
