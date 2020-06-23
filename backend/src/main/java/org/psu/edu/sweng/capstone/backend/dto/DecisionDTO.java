package org.psu.edu.sweng.capstone.backend.dto;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;
import org.psu.edu.sweng.capstone.backend.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DecisionDTO {

    private Long id;
    private String name;
    private String description;
    private Date createdDate;
    private Date expirationDate;
    private Date updatedDate;
    private String ownerUsername;
    
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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

	public static DecisionDTO build(Decision d) {
        DecisionDTO dto = new DecisionDTO();

        if (d.getId() != null) { dto.setId(d.getId()); }
        if (d.getName() != null) { dto.setName(d.getName()); }
        if (d.getDescription() != null) { dto.setDescription(d.getDescription()); }
        if (d.getExpirationDate() != null) { dto.setExpirationDate(d.getExpirationDate()); }
        if (d.getCreatedDate() != null) { dto.setCreatedDate(d.getCreatedDate()); }
        if (d.getUpdatedDate() != null) { dto.setUpdatedDate(d.getUpdatedDate()); }
        if (d.getOwnerId() != null) { dto.setOwnerUsername(d.getOwnerId().getUserName()); }

        for (DecisionUser decisionUser : d.getDecisionUsers()) {
        	UserDTO usrDto = new UserDTO();
        	final User user = decisionUser.getUser();
        	
        	usrDto.setUserName(user.getUserName());
        	usrDto.setFirstName(user.getFirstName());
        	usrDto.setLastName(user.getLastName());
        	
            dto.getIncludedUsers().add(usrDto);
        }

        return dto;
    }
}
