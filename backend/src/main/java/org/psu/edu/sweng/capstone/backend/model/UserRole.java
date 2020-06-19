package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.psu.edu.sweng.capstone.backend.model.id.UserRoleId;

@Entity
@Table(name = "USER_ROLES")
@IdClass(UserRoleId.class)
public class UserRole {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	
	protected UserRole() {}
	
	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}
