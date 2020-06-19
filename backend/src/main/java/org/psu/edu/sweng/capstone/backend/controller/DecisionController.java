package org.psu.edu.sweng.capstone.backend.controller;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/decision")
public class DecisionController {
	
	@Autowired
	private DecisionService decisionService;
	
	@GetMapping("/users/{id}")
	public List<UserDTO> getUsers(@PathVariable(value = "id") Long id) {
		return decisionService.getUsers(id);
	}

	@PostMapping("")
	public String createDecision(@RequestBody final DecisionDTO decision) {
		return decisionService.createDecision(decision);
	}

	@DeleteMapping("/{id}")
	public String deleteDecision(@PathVariable(value = "id") Long id){
		return decisionService.deleteDecision(id);
	}

	@GetMapping("/{id}")
	public DecisionDTO getDecision(@PathVariable(value = "id") Long id){
		return decisionService.getDecision(id);
	}

	@PutMapping("/{id}")
	public String updateDecision(@PathVariable(value = "id") Long id, @RequestBody final DecisionDTO decision){
		return decisionService.updateDecision(id, decision);
	}

}
