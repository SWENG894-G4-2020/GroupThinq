package org.psu.edu.sweng.capstone.backend.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.model.UserRole;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
	
	private String userName;
	private String password;
	private String lastName;
	private String firstName;
	private String emailAddress;
	private Date birthDate;
	private Date createdDate;
	private Date updatedDate;
	private Date lastLoggedIn;
	
	private List<String> userRoles = new ArrayList<>();
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public List<String> getUserRoles() {
		return userRoles;
	}

	public static UserDTO buildDTO(User u) {
		UserDTO dto = new UserDTO();
		
		if (u.getUserName() != null) { dto.setUserName(u.getUserName()); }
		if (u.getLastName() != null) { dto.setLastName(u.getLastName()); }
		if (u.getFirstName() != null) { dto.setFirstName(u.getFirstName()); }
		if (u.getEmailAddress() != null) { dto.setEmailAddress(u.getEmailAddress()); }
		if (u.getBirthDate() != null) { dto.setBirthDate(u.getBirthDate()); }
		if (u.getCreatedDate() != null) { dto.setCreatedDate(u.getCreatedDate()); }
		if (u.getUpdatedDate() != null) { dto.setUpdatedDate(u.getUpdatedDate()); }
		if (u.getLastLoggedIn() != null) { dto.setLastLoggedIn(u.getLastLoggedIn()); }
		
		for (UserRole userRole : u.getUserRoles()) {
			dto.getUserRoles().add(userRole.getRole().getName());
		}
		
		return dto;
	}
}
