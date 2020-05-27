package org.psu.edu.sweng.capstone.backend.controller;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/decision")
public class DecisionController {
	
	@Autowired
	private DecisionService decisionService;
	
	@GetMapping("/{id}")
	public List<UserDTO> getUsers(@PathVariable(value = "id") Long id) {
		return decisionService.getUsers(id);
	}
}
