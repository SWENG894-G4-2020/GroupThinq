package org.psu.edu.sweng.capstone.backend.helper;

public class UniquePair {

	private Long optionOne;
	private Long optionTwo;
	
	public UniquePair(Long optionOne, Long optionTwo) {
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
	}

	public Long getOptionOne() {
		return optionOne;
	}

	public void setOptionOne(Long optionOne) {
		this.optionOne = optionOne;
	}

	public Long getOptionTwo() {
		return optionTwo;
	}

	public void setOptionTwo(Long optionTwo) {
		this.optionTwo = optionTwo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[" + this.getOptionOne().toString() + ", " + this.getOptionTwo().toString() + "]");
		
		return builder.toString();
	}
}
