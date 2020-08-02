package org.psu.edu.sweng.capstone.backend.enumeration;

public enum BallotTypeEnum {
	SINGLE_CHOICE("Single-Choice"),
	RANKED_PAIR("Ranked-Pair");
	
	private String description;
	
	private BallotTypeEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
