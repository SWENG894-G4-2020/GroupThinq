package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;

public class UserRoleId implements Serializable {
	private static final long serialVersionUID = 1736396020700157582L;

	private Long user;
	private Long role;

	public UserRoleId() {}
	
	public UserRoleId(Long user, Long role) {
		this.user = user;
		this.role = role;
	}

	public Long getUser() {
		return user;
	}

	public Long getRole() {
		return role;
	}
}
