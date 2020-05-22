package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

	@Column(name = "USERNAME")
	@Id
	private String userName;
	
	@Column(name = "LASTNAME")
	private String lastName;
	
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@Column(name = "EMAIL")
	private String emailAddress;
	
	protected User() {}
	
	public User(String userName, String lastName, String firstName, String emailAddress) {
		this.userName = userName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.emailAddress = emailAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
}
