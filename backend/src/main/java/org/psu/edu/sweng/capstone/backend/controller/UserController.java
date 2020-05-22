package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public String getUsers() {
		return userService.getUsers();
	}
	
	@GetMapping("/user/{userName}")
	public String getUser(@PathVariable(value = "userName") String userName) {
		return userService.getUser(userName);
	}
	
	@GetMapping("/user/delete/{userName}")
	public String deleteUser(@PathVariable(value = "userName") String userName) {
		return userService.deleteUser(userName);
	}
	
	@GetMapping("/user/update/{userName}")
	public String updateUser(@PathVariable(value = "userName") String userName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "email", required = false) String email) {
		
		return userService.updateUser(userName, lastName, firstName, email);
	}
	
	@GetMapping("/user/create/{userName}")
	public String createUser(@PathVariable(value = "userName") String userName,
			@RequestParam(value = "lastName", required = true) String lastName,
			@RequestParam(value = "firstName", required = true) String firstName,
			@RequestParam(value = "email", required = true) String email) {
		
		return userService.createUser(userName, lastName, firstName, email);
	}
}
