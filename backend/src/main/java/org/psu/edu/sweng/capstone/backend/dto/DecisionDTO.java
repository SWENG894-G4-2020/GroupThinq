package org.psu.edu.sweng.capstone.backend.dto;

import org.psu.edu.sweng.capstone.backend.model.Decision;
import org.psu.edu.sweng.capstone.backend.model.DecisionUser;

import java.util.ArrayList;
import java.util.Date;

public class DecisionDTO {

    private Long id;
    private String decisionName;
    private ArrayList<String> includedUsers;
    private Date createdDate;
    private Date expirationDate;
    private Date updatedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecisionName() {
        return decisionName;
    }

    public void setDecisionName(String decisionName) {
        this.decisionName = decisionName;
    }

    public ArrayList<String> getIncludedUsers() {
        return includedUsers;
    }

    public void setIncludedUsers(ArrayList<String> includedUsers) {
        this.includedUsers = includedUsers;
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

    public static DecisionDTO buildDTO(Decision d) {
        DecisionDTO dto = new DecisionDTO();

        if(d.getId() != null) { dto.setId(d.getId());}
        if (d.getName() != null) { dto.setDecisionName(d.getName()); }
        if (d.getExpirationDate() != null) { dto.setExpirationDate(d.getExpirationDate()); }
        if (d.getCreatedDate() != null) { dto.setCreatedDate(d.getCreatedDate()); }
        if (d.getUpdatedDate() != null) { dto.setUpdatedDate(d.getUpdatedDate()); }

        for (DecisionUser decisionUser : d.getDecisionUsers()) {
            dto.getIncludedUsers().add(decisionUser.getUser().getUserName());
        }

        return dto;
    }
}
