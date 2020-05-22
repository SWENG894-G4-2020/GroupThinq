package org.psu.edu.sweng.capstone.backend.impl;

public interface UserService {
	String getUsers();
	String getUser(String userName);
	String deleteUser(String userName);
	String updateUser(String userName, String lastName, String firstName, String email);
	String createUser(String userName, String lastName, String firstName, String email);
}
