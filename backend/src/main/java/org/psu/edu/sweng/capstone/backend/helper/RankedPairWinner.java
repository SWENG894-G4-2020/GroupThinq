package org.psu.edu.sweng.capstone.backend.helper;

public class RankedPairWinner {
	
	private UniquePair uniquePair;
	private String winningOption;
	private int voteDifference;
	
	public RankedPairWinner(UniquePair uniquePair, String option, int voteDifference) {
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

	public String getWinningOption() {
		return winningOption;
	}

	public void setWinningOption(String option) {
		this.winningOption = option;
	}

	public int getVoteDifference() {
		return voteDifference;
	}

	public void setVoteDifference(int voteDifference) {
		this.voteDifference = voteDifference;
	}
}
