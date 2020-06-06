package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;
import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(this);
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId)) return false;
        
        UserRoleId userRoleId = (UserRoleId) o;
        
        return (Objects.equals(this.getRole(), userRoleId.getRole()) &&
        		Objects.equals(this.getUser(), userRoleId.getUser()));
	}
}
