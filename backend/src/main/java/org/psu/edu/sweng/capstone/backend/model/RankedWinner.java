package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.psu.edu.sweng.capstone.backend.model.id.RankedWinnerId;

@Entity
@IdClass(RankedWinnerId.class)
@Table(name = "RANKED_WINNERS")
public class RankedWinner {

	@Id
	@OneToOne
	@JoinColumn(name = "BALLOT_ID")
	private Ballot ballot;
	
	@Id
	@OneToOne
	@JoinColumn(name = "WINNER_ID")
	private BallotOption winner;
	
	protected RankedWinner() {}
	
	public RankedWinner(Ballot ballot, BallotOption winner) {
		this.ballot = ballot;
		this.winner = winner;
	}

	public Ballot getBallot() {
		return ballot;
	}

	public void setBallot(Ballot ballot) {
		this.ballot = ballot;
	}

	public BallotOption getWinner() {
		return winner;
	}

	public void setWinner(BallotOption winner) {
		this.winner = winner;
	}
}
