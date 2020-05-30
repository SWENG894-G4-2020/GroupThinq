package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;

import org.psu.edu.sweng.capstone.backend.model.Role;
import org.psu.edu.sweng.capstone.backend.model.User;

public class UserRoleId implements Serializable {
	private static final long serialVersionUID = 1736396020700157582L;

	private User user;
	private Role role;

	public UserRoleId() {}
	
	public UserRoleId(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}
}
