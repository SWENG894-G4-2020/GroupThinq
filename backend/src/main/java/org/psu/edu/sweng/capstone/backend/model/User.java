package org.psu.edu.sweng.capstone.backend.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@Column(name = "PW")
	private String password;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "EMAIL")
	private String emailAddress;
	
	@Column(name = "BIRTH_DATE")
	private Date birthDate;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "LAST_LOGGED_IN")
	private Date lastLoggedIn;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = UserRole.class)
	@Fetch(FetchMode.SELECT)
	private Set<UserRole> userRoles = new HashSet<>();
	
	protected User() {}
	
	public User(String userName, String password, String lastName, String firstName, String emailAddress, Date birthDate, Date createdDate) {
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
	
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(this.firstName).append(" ").append(this.lastName);
		
		return builder.toString();
	}
}
