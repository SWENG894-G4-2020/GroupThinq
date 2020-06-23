package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.*;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecisionServiceImpl implements DecisionService {

	private static final String DECISION_STRING = "Decision ";
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private DecisionDAO decisionDao;
	
	@Autowired
	private DecisionUserDAO decisionUserDao;
	
	@Override
	public List<UserDTO> getUsers(Long id) {
		List<UserDTO> userList = new ArrayList<>();
		Optional<Decision> dec = decisionDao.findById(id);
		
		if (dec.isPresent()) {			
			for (DecisionUser du : dec.get().getDecisionUsers()) {
				UserDTO dto = UserDTO.build(du.getUser());
				userList.add(dto);
			}
		}
		
		return userList;
	}

	@Override
	public String updateDecision(Long id, DecisionDTO decisionDTO) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		if (!decisionOpt.isPresent()) {
			return "Decision does not exist";
		}

		Decision decision = decisionOpt.get();

		if (decisionDTO.getName() != null) { decision.setName(decisionDTO.getName()); }
		if (decisionDTO.getDescription() != null) { decision.setDescription(decisionDTO.getDescription()); }
		if (decisionDTO.getExpirationDate() != null) { decision.setExpirationDate(decisionDTO.getExpirationDate()); }
		
		if (decisionDTO.getIncludedUsers() != null) {
			decisionUserDao.deleteAllByDecision(decision);
			
			for (UserDTO usrDTO : decisionDTO.getIncludedUsers()) {
				Optional<User> user = userDao.findByUserName(usrDTO.getUserName());
				
				if (user.isPresent()) {
					decision.getDecisionUsers().add(new DecisionUser(decision, user.get()));
				}
			}
		}

		decision.setUpdatedDate(new Date());

		decisionDao.save(decision);

		StringBuilder builder = new StringBuilder();
		builder.append(DECISION_STRING).append(id).append(" has been updated.");

		return builder.toString();
	}

	@Override
	public String createDecision(DecisionDTO decisionDTO) {
		Optional<User> user = userDao.findByUserName(decisionDTO.getOwnerUsername());

		StringBuilder builder = new StringBuilder();
		if (user.isPresent()) {
			Decision newDecision = new Decision(
					decisionDTO.getName(),
					decisionDTO.getDescription(),
					decisionDTO.getExpirationDate(),
					user.get()
			);
			
			Set<DecisionUser> decisionUsers = new HashSet<>();
			
			for (UserDTO dto : decisionDTO.getIncludedUsers()) {
				Optional<User> includedUser = userDao.findByUserName(dto.getUserName());
				
				if (includedUser.isPresent()) {
					decisionUsers.add(new DecisionUser(newDecision, includedUser.get()));
				}
			}
			
			newDecision.setDecisionUsers(decisionUsers);
	
			newDecision.setCreatedDate(new Date());
	
			decisionDao.save(newDecision);
			
			builder.append(DECISION_STRING).append("has been created.");
		}
		else {
			builder.append(DECISION_STRING).append("could not be created.");
		}
		
		return builder.toString();
	}

	@Override
	public String deleteDecision(Long id) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		if (!decisionOpt.isPresent()) {
			return "Decision does not exist";
		}

		Decision decision = decisionOpt.get();

		decision.getDecisionUsers().clear();

		decisionDao.delete(decision);

		StringBuilder builder = new StringBuilder();
		builder.append(DECISION_STRING).append(id).append(" has been deleted.");

		return builder.toString();
	}

	@Override
	public DecisionDTO getDecision(Long id) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		return decisionOpt.map(DecisionDTO::build).orElse(null);
	}
}
