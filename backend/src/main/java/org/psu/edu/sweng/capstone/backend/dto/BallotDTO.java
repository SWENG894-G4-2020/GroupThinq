package org.psu.edu.sweng.capstone.backend.dto;

import java.util.Date;

import org.psu.edu.sweng.capstone.backend.model.Ballot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BallotDTO {
	
	private Long id;
	private Long decisionId;
	private Date expirationDate;
	private Date createdDate;
	private Date updatedDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDecisionId() {
		return decisionId;
	}
	
	public void setDecisionId(Long decisionId) {
		this.decisionId = decisionId;
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
	
	public static BallotDTO build(Ballot b) {
		BallotDTO dto = new BallotDTO();
		
		if (b.getId() != null) { dto.setId(b.getId()); }
        if (b.getDecision() != null) { dto.setDecisionId(b.getDecision().getId()); }
        if (b.getExpirationDate() != null) { dto.setExpirationDate(b.getExpirationDate()); }
        if (b.getCreatedDate() != null) { dto.setCreatedDate(b.getCreatedDate()); }
        if (b.getUpdatedDate() != null) { dto.setUpdatedDate(b.getUpdatedDate()); }
	        
		return dto;
	}
}
