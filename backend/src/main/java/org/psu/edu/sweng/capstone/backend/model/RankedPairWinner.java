package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RANKED_PAIR_WINNERS")
public class RankedPairWinner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BALLOT_ID")
	private Ballot ballot;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "WINNING_OPTION_ID")
	private BallotOption winner;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOSING_OPTION_ID")
	private BallotOption loser;
	
	@Column(name = "MARGIN")
	private Long margin;
	
	public RankedPairWinner(Ballot ballot, BallotOption winner, BallotOption loser, Long margin) {
		this.ballot = ballot;
		this.winner = winner;
		this.loser = loser;
		this.margin = margin;
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

	public BallotOption getWinner() {
		return winner;
	}

	public void setWinner(BallotOption winner) {
		this.winner = winner;
	}

	public BallotOption getLoser() {
		return loser;
	}

	public void setLoser(BallotOption loser) {
		this.loser = loser;
	}

	public Long getMargin() {
		return margin;
	}

	public void setMargin(Long margin) {
		this.margin = margin;
	}
}
