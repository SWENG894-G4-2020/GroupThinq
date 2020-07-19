package org.psu.edu.sweng.capstone.backend.dto;

import java.util.Date;

import org.psu.edu.sweng.capstone.backend.model.BallotOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BallotOptionDTO {

	private Long id;
	private Long ballotId;
	private String userName;
	private String title;
	private String description;
	private Date createdDate;
	private Date updatedDate;
	
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
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public static BallotOptionDTO build(BallotOption bo) {
		BallotOptionDTO dto = new BallotOptionDTO();
		
		if (bo.getId() != null) { dto.setId(bo.getId()); }
		if (bo.getBallot() != null) { dto.setBallotId(bo.getBallot().getId()); }
		if (bo.getUser() != null) { dto.setUserName(bo.getUser().getUserName()); }
		if (bo.getTitle() != null) { dto.setTitle(bo.getTitle()); }
		if (bo.getDescription() != null) { dto.setDescription(bo.getDescription()); }
		if (bo.getCreatedDate() != null) { dto.setCreatedDate(bo.getCreatedDate()); }
		if (bo.getUpdatedDate() != null) { dto.setUpdatedDate(bo.getUpdatedDate()); }
		
		return dto;
	}
}
