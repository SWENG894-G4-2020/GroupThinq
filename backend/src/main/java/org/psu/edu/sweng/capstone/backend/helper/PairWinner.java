package org.psu.edu.sweng.capstone.backend.helper;

public class PairWinner {
	private String option;
	private int voteDifference;
	
	public PairWinner(String option, int voteDifference) {
		this.option = option;
		this.voteDifference = voteDifference;
	}

	protected PairWinner() {}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getVoteDifference() {
		return voteDifference;
	}

	public void setVoteDifference(int voteDifference) {
		this.voteDifference = voteDifference;
	}
}