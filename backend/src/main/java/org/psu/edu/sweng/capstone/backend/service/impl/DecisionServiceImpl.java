package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.BallotDTO;
import org.psu.edu.sweng.capstone.backend.dto.BallotOptionDTO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.exception.EntityNotFoundException;
import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;
import org.psu.edu.sweng.capstone.backend.service.BallotService;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecisionServiceImpl implements DecisionService {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private BallotDAO ballotDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	@Autowired
	private BallotService ballotService;
	
	@Autowired
	private DecisionUserDAO decisionUserDao;
	
	@Autowired
	private BallotOptionDAO ballotOptionDao;
	
	private static final String ERROR_HEADER = "Decision ";

	@Override
	public ResponseEntity<UserDTO> getUsers(final Long id) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final Decision dec = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + id.toString()));
		
		dec.getDecisionUsers().stream().forEach(du -> response.getData().add(UserDTO.build(du.getUser())));
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> updateDecision(final Long id, final DecisionDTO decisionDto) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Decision decision = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + id.toString()));
		
		if (decisionDto.getName() != null) { decision.setName(decisionDto.getName()); }
		if (decisionDto.getDescription() != null) { decision.setDescription(decisionDto.getDescription()); }
		if (decisionDto.getIncludedUsers() != null) { wipeAndAddNewDecisionUsers(decision, decisionDto.getIncludedUsers()); }
		
		if (!decisionDto.getBallots().isEmpty()) {
			for (BallotDTO ballotDTO : decisionDto.getBallots()) {
				ballotService.updateBallot(ballotDTO.getId(), ballotDTO);
			}
		}

		decision.setUpdatedDate(new Date());

		decisionDao.save(decision);
		
		response.attachGenericSuccess();
		
		return response;
	}
	
	@Override
	public ResponseEntity<DecisionDTO> createDecision(final DecisionDTO decisionDto) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final User user = userDao.findByUserName(decisionDto.getOwnerUsername()).orElseThrow(
				() -> new EntityNotFoundException("User " + decisionDto.getOwnerUsername()));
		
		// Create new Decision
		Decision newDecision = new Decision(
				decisionDto.getName(),
				decisionDto.getDescription(),
				user
		);
			
		// Add Users
		Set<DecisionUser> decisionUsers = new HashSet<>();
		for (UserDTO dto : decisionDto.getIncludedUsers()) {
			Optional<User> includedUser = userDao.findByUserName(dto.getUserName());
			
			if (includedUser.isPresent()) {
				decisionUsers.add(new DecisionUser(newDecision, includedUser.get()));
			}
		}
		
		newDecision.setDecisionUsers(decisionUsers);

		decisionDao.save(newDecision);
	
		if (!decisionDto.getBallots().isEmpty()) {
			// Add Ballots
			for (BallotDTO bDTO : decisionDto.getBallots()) {
				Ballot ballot = new Ballot(newDecision, bDTO.getExpirationDate());
				ballotDao.save(ballot);
				
				// Add Ballot Options
				for (BallotOptionDTO boDTO : bDTO.getBallotOptions()) {
					BallotOption ballotOption = new BallotOption(boDTO.getTitle(), boDTO.getDescription(), ballot, user);
					ballotOptionDao.save(ballotOption);
				}
			}
		}
		
		response.attachCreatedSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> deleteDecision(final Long id) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Decision decision = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + id.toString()));

		decision.getDecisionUsers().clear();

		decisionDao.delete(decision);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecision(final Long id) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Decision decision = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(ERROR_HEADER + id.toString()));
		
		response.getData().add(DecisionDTO.build(decision));
		response.attachGenericSuccess();
		
		return response;
	}
	
	private void wipeAndAddNewDecisionUsers(Decision decision, final List<UserDTO> includedUsers) {
		decisionUserDao.deleteAllByDecision(decision);
		
		for (UserDTO usrDTO : includedUsers) {
			Optional<User> user = userDao.findByUserName(usrDTO.getUserName());
			if (user.isPresent()) { decision.getDecisionUsers().add(new DecisionUser(decision, user.get())); }
		}
	}
}
