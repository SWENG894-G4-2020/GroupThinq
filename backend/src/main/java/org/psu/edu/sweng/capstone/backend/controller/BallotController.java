package org.psu.edu.sweng.capstone.backend.controller;

import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.BallotResultDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.service.BallotOptionService;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	@Autowired
	private BallotOptionService ballotOptionService;
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@PostMapping("/ballot")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BallotDTO> createBallot(@RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.createBallot(ballot);
	}
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@DeleteMapping("ballot/{id}")
	public ResponseEntity<BallotDTO> deleteBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.deleteBallot(ballotId);
	}
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@GetMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> retrieveBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.retrieveBallot(ballotId);
	}
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@PutMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> updateBallot(@PathVariable(value = "id") final Long ballotId, @RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.updateBallot(ballotId, ballot);
	}
	
	@PreAuthorize("@authCheck.votingActive(#vote.getBallotId())")
	@PostMapping("/ballot/{id}/vote")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> castVote(@RequestBody final BallotResultDTO vote) throws EntityNotFoundException {
		return ballotService.castVote(vote);
	}
	
	@PreAuthorize("@authCheck.votingActive(#vote.getBallotId())")
	@PutMapping("/ballot/{id}/vote")
	public ResponseEntity<String> updateVote(@RequestBody final BallotResultDTO vote) throws EntityNotFoundException {
		return ballotService.updateVote(vote);
	}
	
	@GetMapping("/ballot/{id}/results")
	public ResponseEntity<BallotResultDTO> retrieveResults(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.retrieveResults(ballotId);
	}
	
	@PreAuthorize("#ballotOption.getUserName() == authentication.getName()")
	@PostMapping("/ballot/{id}/option")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createBallotOption(@RequestBody final BallotOptionDTO ballotOption) throws EntityNotFoundException {
		return ballotOptionService.createBallotOption(ballotOption);
	}
}
