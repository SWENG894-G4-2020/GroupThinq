package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityConflictException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/users")
	public ResponseEntity<UserDTO> getUsers() {
		return userService.getUsers();
	}
	
	@PreAuthorize("@authCheck.hasUserAccess(#username) or @authCheck.isAdmin()")
	@GetMapping("/user/{username}")
	public ResponseEntity<UserDTO> getUser(@PathVariable(value = "username") final String username) throws EntityNotFoundException {
		return userService.getUser(username);
	}
	
	@PreAuthorize("@authCheck.hasUserAccess(#username) or @authCheck.isAdmin()")
	@DeleteMapping("/user/{username}")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable(value = "username") final String username) throws EntityNotFoundException {
		return userService.deleteUser(username);
	}
	
	@PreAuthorize("@authCheck.hasUserAccess(#username) or @authCheck.isAdmin()")
	@PutMapping("/user/{username}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable(value = "username") final String username, @RequestBody final UserDTO user) 
			throws EntityNotFoundException {
		return userService.updateUser(username, user);
	}
	
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<UserDTO> createUser(@RequestBody final UserDTO user) throws EntityConflictException {
		return userService.createUser(user);
	}
	
	@PreAuthorize("@authCheck.hasUserAccess(#username)")
	@GetMapping("/user/{username}/decisions")
	public ResponseEntity<DecisionDTO> getUserDecisions(@PathVariable(value = "username") final String username) throws EntityNotFoundException {
		return userService.getDecisions(username);
	}
}
