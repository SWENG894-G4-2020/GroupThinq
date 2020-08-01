package org.psu.edu.sweng.capstone.backend.helper;

public class RankedPairWinner {
	
	private UniquePair uniquePair;
	private Long winningOption;
	private int voteDifference;
	
	public RankedPairWinner(UniquePair uniquePair, Long option, int voteDifference) {
		this.uniquePair = uniquePair;
		this.winningOption = option;
		this.voteDifference = voteDifference;
	}

	protected RankedPairWinner() {}

	public UniquePair getUniquePair() {
		return uniquePair;
	}

	public void setUniquePair(UniquePair uniquePair) {
		this.uniquePair = uniquePair;
	}

	public Long getWinningOption() {
		return winningOption;
	}

	public void setWinningOption(Long option) {
		this.winningOption = option;
	}

	public int getVoteDifference() {
		return voteDifference;
	}

	public void setVoteDifference(int voteDifference) {
		this.voteDifference = voteDifference;
	}
}
