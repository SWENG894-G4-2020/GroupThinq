package org.psu.edu.sweng.capstone.backend.controller;

import java.util.List;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.RankedWinnerDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.enumeration.BallotTypeEnum;
import org.psu.edu.sweng.capstone.backend.dto.BallotVoteDTO;
import org.psu.edu.sweng.capstone.backend.dto.RankedWinnerDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.RankedWinner;
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
	private BallotDAO ballotDao;
		
	@Autowired
	private BallotService ballotService;
	
	@Autowired
	private RankedWinnerDAO rankedWinnerDao;
	
	@Autowired
	private BallotOptionService ballotOptionService;
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@PostMapping("/ballot")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BallotDTO> createBallot(@RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.createBallot(ballot);
	}
	
	@PreAuthorize("@authCheck.doesUserOwnDecisionUnderBallot(#ballotId) or @authCheck.isAdmin()")
	@DeleteMapping("ballot/{id}")
	public ResponseEntity<BallotDTO> deleteBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.deleteBallot(ballotId);
	}
	
	@PreAuthorize("@authCheck.doesUserOwnDecisionUnderBallot(#ballotId) or @authCheck.isAdmin()")
	@GetMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> retrieveBallot(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		return ballotService.retrieveBallot(ballotId);
	}
	
	@PreAuthorize("@authCheck.isDecisionOwner(#ballot.getDecisionId()) or @authCheck.isAdmin()")
	@PutMapping("/ballot/{id}")
	public ResponseEntity<BallotDTO> updateBallot(@PathVariable(value = "id") final Long ballotId, @RequestBody final BallotDTO ballot) throws EntityNotFoundException {
		return ballotService.updateBallot(ballotId, ballot);
	}
	
	@PreAuthorize("@authCheck.votingActive(#vote)")
	@PostMapping("/ballot/{id}/vote")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> castVote(@RequestBody final List<BallotVoteDTO> vote) throws EntityNotFoundException {
		return ballotService.castVote(vote);
	}
	
	@PreAuthorize("@authCheck.votingActive(#vote)")
	@PutMapping("/ballot/{id}/vote")
	public ResponseEntity<String> updateVote(@RequestBody final List<BallotVoteDTO> vote) throws EntityNotFoundException {
		return ballotService.updateVote(vote);
	}

	@GetMapping("/ballot/{id}/votes")
	public ResponseEntity<BallotVoteDTO> retrieveVotes(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException("Ballot " + ballotId));
		return ballotService.retrieveAllVotes(ballot);
	}
	
	@PreAuthorize("@authCheck.votingHasExpired(#ballotId)")
	@GetMapping("/ballot/{id}/results")
	public ResponseEntity<?> retrieveResults(@PathVariable(value = "id") final Long ballotId) throws EntityNotFoundException {
		final Ballot ballot = ballotDao.findById(ballotId).orElseThrow(
				() -> new EntityNotFoundException("Ballot " + ballotId));
		
		if (BallotTypeEnum.SINGLE_CHOICE.getDescription().equals(ballot.getType().getName())) {
			return ballotService.retrieveAllVotes(ballot);
		}
		else {
			ResponseEntity<RankedWinnerDTO> response = new ResponseEntity<>();
			
			ballotService.retrieveRankedChoiceResults(ballot);
			
			final RankedWinner rankedWinner = rankedWinnerDao.findByBallot(ballot).orElseThrow(
					() -> new EntityNotFoundException("Ranked Winner from Ballot " + ballot.getId()));
			
			response.attachGenericSuccess();
			response.getData().add(RankedWinnerDTO.build(rankedWinner.getBallot(), rankedWinner.getWinner()));
			
			return response;
		}
	}
	
	@PreAuthorize("#ballotOption.getUserName() == authentication.getName()")
	@PostMapping("/ballot/{id}/option")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createBallotOption(@RequestBody final BallotOptionDTO ballotOption) throws EntityNotFoundException {
		return ballotOptionService.createBallotOption(ballotOption);
	}
}
