package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
import org.psu.edu.sweng.capstone.backend.dao.UserDAO;
import org.psu.edu.sweng.capstone.backend.dto.DecisionDTO;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;
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
	public ResponseEntity<UserDTO> getUsers(Long id) {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Decision> dec = decisionDao.findById(id);
			
			if (dec.isPresent()) {			
				for (DecisionUser du : dec.get().getDecisionUsers()) {
					UserDTO dto = UserDTO.build(du.getUser());
					response.getData().add(dto);
				}
				
				response.attachGenericSuccess();
			}
			else {
				response.attachEntityNotFound(DECISION_STRING + id.toString());
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> updateDecision(Long id, DecisionDTO decisionDto) {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Decision> decisionOpt = decisionDao.findById(id);
			
			if (!decisionOpt.isPresent()) {
				response.attachEntityNotFound(DECISION_STRING + id.toString());
			}
			else {
				final Decision decision = decisionOpt.get();

				if (decisionDto.getName() != null) { decision.setName(decisionDto.getName()); }
				if (decisionDto.getDescription() != null) { decision.setDescription(decisionDto.getDescription()); }
				if (decisionDto.getIncludedUsers() != null) { wipeAndAddNewDecisionUsers(decision, decisionDto.getIncludedUsers()); }

				decision.setUpdatedDate(new Date());

				decisionDao.save(decision);
				
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}
	
	@Override
	public ResponseEntity<DecisionDTO> createDecision(DecisionDTO decisionDto) {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		try {
			Optional<User> user = userDao.findByUserName(decisionDto.getOwnerUsername());
			
			if (!user.isPresent()) {
				response.attachEntityNotFound(decisionDto.getOwnerUsername());
			}
			else {
				Decision newDecision = new Decision(
						decisionDto.getName(),
						decisionDto.getDescription(),
						user.get()
				);
				
				Set<DecisionUser> decisionUsers = new HashSet<>();
				
				for (UserDTO dto : decisionDto.getIncludedUsers()) {
					Optional<User> includedUser = userDao.findByUserName(dto.getUserName());
					
					if (includedUser.isPresent()) {
						decisionUsers.add(new DecisionUser(newDecision, includedUser.get()));
					}
				}
				
				newDecision.setDecisionUsers(decisionUsers);
				newDecision.setCreatedDate(new Date());
		
				decisionDao.save(newDecision);
				
				response.attachCreatedSuccess();
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> deleteDecision(Long id) {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Decision> decisionOpt = decisionDao.findById(id);
	
			if (!decisionOpt.isPresent()) {
				response.attachEntityNotFound(DECISION_STRING + id.toString());
			}
			else {
				final Decision decision = decisionOpt.get();
	
				decision.getDecisionUsers().clear();
	
				decisionDao.delete(decision);
				
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}

	@Override
	public ResponseEntity<DecisionDTO> getDecision(Long id) {
		ResponseEntity<DecisionDTO> response = new ResponseEntity<>();
		
		try {
			Optional<Decision> decisionOpt = decisionDao.findById(id);
			
			if (!decisionOpt.isPresent()) {
				response.attachEntityNotFound(DECISION_STRING + id.toString());
			}
			else {
				response.getData().add(DecisionDTO.build(decisionOpt.get()));
				response.attachGenericSuccess();
			}
		}
		catch (Exception e) {
			response.attachExceptionError(e);
		}
		
		return response;
	}
	
	private void wipeAndAddNewDecisionUsers(Decision decision, List<UserDTO> includedUsers) {
		decisionUserDao.deleteAllByDecision(decision);
		
		for (UserDTO usrDTO : includedUsers) {
			Optional<User> user = userDao.findByUserName(usrDTO.getUserName());
			
			if (user.isPresent()) {
				decision.getDecisionUsers().add(new DecisionUser(decision, user.get()));
			}
		}
	}
}
