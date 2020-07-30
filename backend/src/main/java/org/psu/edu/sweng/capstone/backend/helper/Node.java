package org.psu.edu.sweng.capstone.backend.helper;

public class Node {
	
	private String option;
	
	public Node (String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	@Override
	public String toString() {
		return this.option.toString();
	}
}
