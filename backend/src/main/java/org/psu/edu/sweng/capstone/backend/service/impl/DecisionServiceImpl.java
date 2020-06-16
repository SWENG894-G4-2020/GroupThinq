package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dao.DecisionUserDAO;
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
				UserDTO dto = UserDTO.buildDTO(du.getUser());
				userList.add(dto);
			}
		}
		
		return userList;
	}

	@Override
	public String updateDecision(Long id, DecisionDTO decisionDto) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		if (!decisionOpt.isPresent()) {
			return "Decision does not exist";
		}

		Decision decision = decisionOpt.get();

		if (decisionDto.getName() != null) { decision.setName(decisionDto.getName()); }
		if (decisionDto.getExpirationDate() != null) { decision.setExpirationDate(decisionDto.getExpirationDate()); }

		decision.setUpdatedDate(new Date());

		decisionDao.save(decision);

		StringBuilder builder = new StringBuilder();
		builder.append("Decision ").append(id).append(" has been updated.");

		return builder.toString();
	}

	@Override
	public String createDecision(Long id, DecisionDTO decisionDto) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		if (decisionOpt.isPresent()) { return "Decision already exists"; }

		Decision newDecision = new Decision(id,
				decisionDto.getName(),
				decisionDto.getExpirationDate(),
				decisionDto.getOwnerId()
		);

		newDecision.setCreatedDate(new Date());

		decisionDao.save(newDecision);

		StringBuilder builder = new StringBuilder();
		builder.append("Decision ").append(id).append(" has been created.");

		return builder.toString();
	}

	@Override
	public String deleteDecision(Long id) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		if (!decisionOpt.isPresent()) {
			return "Decision does not exist";
		}

		Decision decision = decisionOpt.get();

		ArrayList<DecisionUser> userDecisions = decisionUserDao.findAllByDecision(decision);

		if (userDecisions.size() > 0) {
			decisionUserDao.deleteAll(userDecisions);
		}

		decisionDao.delete(decision);

		StringBuilder builder = new StringBuilder();
		builder.append("Decision ").append(id).append(" has been deleted.");

		return builder.toString();
	}

	@Override
	public DecisionDTO getDecision(Long id) {
		Optional<Decision> decisionOpt = decisionDao.findById(id);

		return decisionOpt.map(DecisionDTO::buildDTO).orElse(null);
	}

}
