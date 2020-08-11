package org.psu.edu.sweng.capstone.backend.dto;

import org.psu.edu.sweng.capstone.backend.model.RankedPairWinner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RankedPairWinnerDTO {
	
	private BallotOptionDTO winner;
	private BallotOptionDTO loser;
	private Long margin;
	
	public static RankedPairWinnerDTO build(final RankedPairWinner rpw) {
		RankedPairWinnerDTO dto = new RankedPairWinnerDTO();
		
		if (rpw.getWinner() != null) { dto.setWinner(BallotOptionDTO.build(rpw.getWinner())); }
		if (rpw.getLoser() != null) { dto.setLoser(BallotOptionDTO.build(rpw.getLoser())); }
		if (rpw.getMargin() != null) { dto.setMargin(rpw.getMargin()); }
		
		return dto;
	}
	
	public BallotOptionDTO getWinner() {
		return winner;
	}
	
	public void setWinner(BallotOptionDTO winner) {
		this.winner = winner;
	}
	
	public BallotOptionDTO getLoser() {
		return loser;
	}
	
	public void setLoser(BallotOptionDTO loser) {
		this.loser = loser;
	}
	
	public Long getMargin() {
		return margin;
	}
	
	public void setMargin(Long margin) {
		this.margin = margin;
	}
}
