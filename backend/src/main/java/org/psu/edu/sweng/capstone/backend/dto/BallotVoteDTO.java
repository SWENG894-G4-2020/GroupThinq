package org.psu.edu.sweng.capstone.backend.dto;

import java.util.Date;

import org.psu.edu.sweng.capstone.backend.model.BallotVote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BallotVoteDTO {

	private Long id;
	private Long ballotId;
	private Long ballotOptionId;
	private String userName;
	private Long rank;
	private Date voteDate;
	private Date voteUpdatedDate;
	
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
	
	public Long getBallotOptionId() {
		return ballotOptionId;
	}
	
	public void setBallotOptionId(Long ballotOptionId) {
		this.ballotOptionId = ballotOptionId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Date getVoteDate() {
		return voteDate;
	}
	
	public void setVoteDate(Date voteDate) {
		this.voteDate = voteDate;
	}
	
	public Date getVoteUpdatedDate() {
		return voteUpdatedDate;
	}

	public void setVoteUpdatedDate(Date voteUpdatedDate) {
		this.voteUpdatedDate = voteUpdatedDate;
	}
	
	public static BallotVoteDTO build(BallotVote br) {
		BallotVoteDTO dto = new BallotVoteDTO();
		
		if (br.getId() != null) { dto.setId(br.getId()); }
		if (br.getBallot() != null) { dto.setBallotId(br.getBallot().getId()); }
		if (br.getBallotOption() != null) { dto.setBallotOptionId(br.getBallotOption().getId()); }
		if (br.getUser() != null) { dto.setUserName(br.getUser().getUserName()); }
		if (br.getRank() != null) { dto.setRank(br.getRank()); }
		if (br.getVoteDate() != null) { dto.setVoteDate(br.getVoteDate()); }
		if (br.getVoteUpdatedDate() != null) { dto.setVoteUpdatedDate(br.getVoteUpdatedDate()); }
		
		return dto;
	}
}
