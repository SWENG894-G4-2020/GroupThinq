package org.psu.edu.sweng.capstone.backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.psu.edu.sweng.capstone.backend.dao.DecisionDAO;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.service.DecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DecisionServiceImpl implements DecisionService {

	@Autowired
	private DecisionDAO decisionDao;
	
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

}
