package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
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
public class BallotController {

	@Autowired
	private BallotService ballotService;
	
	@PostMapping("/ballot")
	public ResponseEntity<BallotDTO> createBallot(@RequestBody final BallotDTO ballot) {
		return ballotService.createBallot(ballot);
	}
	
	@DeleteMapping("ballot/{id}")
	public ResponseEntity<BallotDTO> deleteBallot(@PathVariable(value = "id") Long ballotId) {
		return ballotService.deleteBallot(ballotId);
	}
	
	@GetMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> retrieveBallot(@PathVariable(value = "id") Long ballotId) {
		return ballotService.retrieveBallot(ballotId);
	}
	
	@PutMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> updateBallot(@PathVariable(value = "id") Long ballotId, @RequestBody final BallotDTO ballot) {
		return ballotService.updateBallot(ballotId, ballot);
	}
}
