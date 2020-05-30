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
	private List<String> userRoles = new ArrayList<>();
	
	public UserDTO() {}
	
	public UserDTO(String userName, String password, String lastName, String firstName, String emailAddress, Date birthDate,
			Date createdDate) {
		this.userName = userName;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.emailAddress = emailAddress;
		this.birthDate = birthDate;
		this.createdDate = createdDate;
	}
	
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

	public List<String> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public static UserDTO buildUserDTO(User u) {
		UserDTO dto = new UserDTO(u.getUserName(), u.getPassword(), u.getLastName(), u.getFirstName(), u.getEmailAddress(), u.getBirthDate(), 
				u.getCreatedDate());
		
		for (UserRole userRole : u.getUserRoles()) {
			dto.getUserRoles().add(userRole.getRole().getName().toString());
		}
		
		return dto;
	}
}
