package org.psu.edu.sweng.capstone.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BallotOptionDTO {

    private Long id;
    private Long ballotId;
    private Long decisionId;
    private String userName;
    private Date expirationDate;
    private Date createdDate;
    private Date updatedDate;
    private String optionTitle;
    private String optionDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBallotId() {
        return ballotId;
    }

    public void setBallotId(Long ballotId) {
        this.ballotId = ballotId;
    }

    public Long getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Long decisionId) {
        this.decisionId = decisionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public static BallotOptionDTO build(BallotOption b) {
        BallotOptionDTO dto = new BallotOptionDTO();

        if (b.getId() != null) { dto.setId(b.getId()); }
        if (b.getBallot() != null) { dto.setBallotId(b.getBallot().getId()); }
        if (b.getDecision() != null) { dto.setDecisionId(b.getDecision().getId()); }
        if (b.getUser() != null) { dto.setUserName(b.getUser().getUserName()); }
        if (b.getExpirationDate() != null) { dto.setExpirationDate(b.getExpirationDate()); }
        if (b.getCreatedDate() != null) { dto.setCreatedDate(b.getCreatedDate()); }
        if (b.getUpdatedDate() != null) { dto.setUpdatedDate(b.getUpdatedDate()); }
        if (b.getOptionTitle() != null) { dto.setOptionTitle(b.getOptionTitle()); }
        if(b.getOptionDescription() != null) { dto.setOptionDescription(b.getOptionDescription()); }

        return dto;
    }
}
