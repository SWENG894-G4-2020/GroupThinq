package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.exception.BallotExpiredException;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class BallotController {

	@Autowired
	private BallotService ballotService;
	
	@PostMapping("/ballot")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BallotDTO> createBallot(@RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.createBallot(ballot);
	}
	
	@DeleteMapping("ballot/{id}")
	public ResponseEntity<BallotDTO> deleteBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.deleteBallot(ballotId);
	}
	
	@GetMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> retrieveBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.retrieveBallot(ballotId);
	}
	
	@PutMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> updateBallot(@PathVariable(value = "id") final Long ballotId, @RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.updateBallot(ballotId, ballot);
	}

	@GetMapping("/ballot/{id}/ballotoptions")
	public ResponseEntity<BallotOptionDTO> retrieveBallotOptions(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.retrieveBallotOptions(ballotId);
	}

	@PostMapping("/ballot/{id}/ballotoptions")
	public ResponseEntity<BallotOptionDTO> addBallotOptions(@PathVariable(value = "id") final Long ballotId, @RequestBody final BallotOptionDTO ballotOption) throws EntityNotFoundException, BallotExpiredException {
		return ballotService.addBallotOptions(ballotId, ballotOption);
	}

	@PostMapping("/ballot/{id}/vote")
	public ResponseEntity<BallotDTO> addVote(@PathVariable(value = "id") final Long ballotId, @RequestBody final Long ballotOptionId, @RequestBody final String userName) throws EntityNotFoundException, BallotExpiredException {
		return ballotService.addVote(ballotId, ballotOptionId, userName);
	}
}
