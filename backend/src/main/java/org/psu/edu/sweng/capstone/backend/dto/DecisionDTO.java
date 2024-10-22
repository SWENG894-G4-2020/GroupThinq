package org.psu.edu.sweng.capstone.backend.dto;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DecisionDTO {

    private Long id;
    private String name;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private String ownerUsername;
    
    private List<BallotDTO> ballots = new ArrayList<>();
    private List<UserDTO> includedUsers = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

	public List<UserDTO> getIncludedUsers() {
        return includedUsers;
    }

    public void setIncludedUsers(List<UserDTO> includedUsers) {
		this.includedUsers = includedUsers;
	}

	public List<BallotDTO> getBallots() {
		return ballots;
	}

	public void setBallots(List<BallotDTO> ballots) {
		this.ballots = ballots;
	}

	public static DecisionDTO build(Decision d) {
        DecisionDTO dto = new DecisionDTO();

        if (d.getId() != null) { dto.setId(d.getId()); }
        if (d.getName() != null) { dto.setName(d.getName()); }
        if (d.getDescription() != null) { dto.setDescription(d.getDescription()); }
        if (d.getCreatedDate() != null) { dto.setCreatedDate(d.getCreatedDate()); }
        if (d.getUpdatedDate() != null) { dto.setUpdatedDate(d.getUpdatedDate()); }
        if (d.getOwner() != null) { dto.setOwnerUsername(d.getOwner().getUserName()); }
        
        d.getBallots().stream().forEach(b -> dto.getBallots().add(BallotDTO.build(b)));
        
        return dto;
    }
	
	public static DecisionDTO buildDecisionUserList(List<DecisionUser> decisionUsers, DecisionDTO dto) {
        decisionUsers.stream().forEach(du -> {
        	UserDTO userDTO = new UserDTO();
        	final User user = du.getUser();
        	
        	userDTO.setUserName(user.getUserName());
        	userDTO.setFirstName(user.getFirstName());
        	userDTO.setLastName(user.getLastName());
        	
            dto.getIncludedUsers().add(userDTO);
        });
		
		return dto;
	}
}
