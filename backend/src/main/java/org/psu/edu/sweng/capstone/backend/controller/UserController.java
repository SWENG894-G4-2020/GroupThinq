package org.psu.edu.sweng.capstone.backend.controller;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("")
	public List<UserDTO> getUsers() {
		return userService.getUsers();
	}
	
	@GetMapping("/{username}")
	public UserDTO getUser(@PathVariable(value = "username") String userName) {
		return userService.getUser(userName);
	}
	
	@DeleteMapping("/{username}")
	public String deleteUser(@PathVariable(value = "username") String userName) {
		return userService.deleteUser(userName);
	}
	
	@PutMapping("/{username}")
	public String updateUser(@PathVariable(value = "username") String userName, @RequestBody final UserDTO user) {
		return userService.updateUser(userName, user);
	}
	
	@PostMapping("/{username}")
	public String createUser(@PathVariable(value = "username") String userName, @RequestBody final UserDTO user) {
		return userService.createUser(userName, user);
	}
	
	@GetMapping("/{username}/decision")
	public List<DecisionDTO> getUserDecisions(@PathVariable(value = "username") String userName, @RequestBody final UserDTO user) {
		return userService.getDecisions(userName, user);
	}
}
