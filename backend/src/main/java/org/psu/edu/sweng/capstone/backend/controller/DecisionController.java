package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DecisionController {
	
	@Autowired
	private DecisionService decisionService;
	
	@PostMapping("/decision")
	public ResponseEntity<DecisionDTO> createDecision(@RequestBody final DecisionDTO decision) {
		return decisionService.createDecision(decision);
	}

	@DeleteMapping("/decision/{id}")
	public ResponseEntity<DecisionDTO> deleteDecision(@PathVariable(value = "id") Long id) {
		return decisionService.deleteDecision(id);
	}

	@GetMapping("/decision/{id}")
	public ResponseEntity<DecisionDTO> getDecision(@PathVariable(value = "id") Long id) {
		return decisionService.getDecision(id);
	}

	@PutMapping("/decision/{id}")
	public ResponseEntity<DecisionDTO> updateDecision(@PathVariable(value = "id") Long id, @RequestBody final DecisionDTO decision) {
		return decisionService.updateDecision(id, decision);
	}
	
	@GetMapping("/decision/{id}/users")
	public ResponseEntity<UserDTO> getUsers(@PathVariable(value = "id") Long id) {
		return decisionService.getUsers(id);
	}
}
