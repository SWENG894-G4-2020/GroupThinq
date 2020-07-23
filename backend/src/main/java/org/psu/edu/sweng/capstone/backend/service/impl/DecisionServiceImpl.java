package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.BallotDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotOptionDAO;
import org.psu.edu.sweng.capstone.backend.dao.BallotTypeDAO;
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
import org.psu.edu.sweng.capstone.backend.model.BallotType;
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
	
	@Autowired
	private BallotTypeDAO ballotTypeDao;
		
	private static final String USER_HEADER = "User ";
	private static final String DECISION_HEADER = "Decision ";

	@Override
	public ResponseEntity<UserDTO> getUsers(final Long id) throws EntityNotFoundException {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		final Decision dec = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(DECISION_HEADER + id.toString()));
		
		decisionUserDao.findAllByDecision(dec).stream().forEach(du -> response.getData().add(UserDTO.build(du.getUser())));
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> updateDecision(final Long id, final DecisionDTO decisionDto) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Decision decision = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(DECISION_HEADER + id.toString()));
		
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
				() -> new EntityNotFoundException(USER_HEADER + decisionDto.getOwnerUsername()));
		
		// Create new Decision
		Decision newDecision = new Decision(decisionDto.getName(), user);		
		decisionDao.save(newDecision);

		// Add Users
		for (UserDTO dto : decisionDto.getIncludedUsers()) {
			final User includedUser = userDao.findByUserName(dto.getUserName()).orElseThrow(
					() -> new EntityNotFoundException(USER_HEADER + dto.getUserName()));
			
			DecisionUser du = new DecisionUser(newDecision, includedUser);
			decisionUserDao.save(du);
		}
	
		if (!decisionDto.getBallots().isEmpty()) {
			// Add Ballots
			for (BallotDTO bDTO : decisionDto.getBallots()) {
				final BallotType type = ballotTypeDao.findById(bDTO.getBallotTypeId()).orElseThrow(
						() -> new EntityNotFoundException("Ballot Type " + bDTO.getBallotTypeId()));
				
				Ballot ballot = new Ballot(newDecision, type, bDTO.getExpirationDate());
				ballotDao.save(ballot);
				
				// Add Ballot Options
				for (BallotOptionDTO boDTO : bDTO.getBallotOptions()) {
					BallotOption ballotOption = new BallotOption(boDTO.getTitle(), ballot, user);
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
				() -> new EntityNotFoundException(DECISION_HEADER + id.toString()));
		
		decision.getBallots().clear();
		
		decisionUserDao.deleteByDecision(decision);

		decisionDao.delete(decision);
		
		response.attachGenericSuccess();
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecision(final Long id) throws EntityNotFoundException {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		final Decision decision = decisionDao.findById(id).orElseThrow(
				() -> new EntityNotFoundException(DECISION_HEADER + id.toString()));
		
		DecisionDTO dto = DecisionDTO.build(decision);
		dto = DecisionDTO.buildDecisionUserList(decisionUserDao.findAllByDecision(decision), dto);
			
		response.getData().add(dto);
		response.attachGenericSuccess();
		
		return response;
	}
	
	private void wipeAndAddNewDecisionUsers(Decision decision, final List<UserDTO> includedUsers) throws EntityNotFoundException {
		ArrayList<DecisionUser> oldDecisionUserList = decisionUserDao.findAllByDecision(decision);
		
		for (UserDTO userDto : includedUsers) {
			final User user = userDao.findByUserName(userDto.getUserName()).orElseThrow(
					() -> new EntityNotFoundException(USER_HEADER + userDto.getUserName()));
			
			Optional<DecisionUser> du = decisionUserDao.findByUserAndDecision(user, decision);			
			
			// Adds User to Decision that didn't previously exist on the Decision User list.
			if (!du.isPresent()) {
				DecisionUser newDu = new DecisionUser(decision, user);
				decisionUserDao.save(newDu);
			}
		}
		
		// If a User existed on the old Decision User list but doesn't exist on the new one, 
		// then delete it from the Decision User list.
		for (DecisionUser du : oldDecisionUserList) {
			boolean deleteUserFromList = true;
			
			User oldUser = du.getUser();
			
			for (UserDTO userDto : includedUsers) {
				if (userDto.getUserName().equals(oldUser.getUserName())) { 
					deleteUserFromList = false;
				}
			}
			
			if (deleteUserFromList) { 
				decisionUserDao.deleteByUserAndDecision(oldUser, decision); 
			}
		}
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecisions() {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		decisionDao.findAll().stream().forEach(d -> response.getData().add(DecisionDTO.build(d)));
				
		response.attachGenericSuccess();
		
		return response;
	}
}
