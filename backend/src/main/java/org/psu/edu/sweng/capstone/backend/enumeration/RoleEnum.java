package org.psu.edu.sweng.capstone.backend.enumeration;

public enum RoleEnum {
	USER("User"),
	ADMIN("Admin");
	
	private String description;
	
	private RoleEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
