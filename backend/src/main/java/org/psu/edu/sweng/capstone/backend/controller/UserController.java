package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<UserDTO> getUsers() {
		return userService.getUsers();
	}
	
	@GetMapping("/user/{username}")
	public ResponseEntity<UserDTO> getUser(@PathVariable(value = "username") String userName) {
		return userService.getUser(userName);
	}
	
	@DeleteMapping("/user/{username}")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable(value = "username") String userName) {
		return userService.deleteUser(userName);
	}
	
	@PutMapping("/user/{username}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "username") String userName, @RequestBody final UserDTO user) {
		return userService.updateUser(userName, user);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/user")
	public ResponseEntity<UserDTO> createUser(@RequestBody final UserDTO user) {
		return userService.createUser(user);
	}
	
	@GetMapping("/user/{username}/decisions")
	public ResponseEntity<DecisionDTO> getUserDecisions(@PathVariable(value = "username") String userName) {
		return userService.getDecisions(userName);
	}
}
