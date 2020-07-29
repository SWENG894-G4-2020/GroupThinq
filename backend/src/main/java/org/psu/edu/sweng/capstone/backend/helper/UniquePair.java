package org.psu.edu.sweng.capstone.backend.helper;

public class UniquePair {

	private String optionOne;
	private String optionTwo;
	
	public UniquePair(String optionOne, String optionTwo) {
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
	}

	public String getOptionOne() {
		return optionOne;
	}

	public void setOptionOne(String optionOne) {
		this.optionOne = optionOne;
	}

	public String getOptionTwo() {
		return optionTwo;
	}

	public void setOptionTwo(String optionTwo) {
		this.optionTwo = optionTwo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[" + this.getOptionOne() + ", " + this.getOptionTwo() + "]");
		
		return builder.toString();
	}
}
