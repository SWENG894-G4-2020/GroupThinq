package org.psu.edu.sweng.capstone.backend.dto;

import java.util.ArrayList;
import java.util.List;

import org.psu.edu.sweng.capstone.backend.model.Ballot;
import org.psu.edu.sweng.capstone.backend.model.BallotOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RankedWinnerDTO {

	private Long ballotId;
	private BallotOptionDTO winner;
	
	private List<RankedPairWinnerDTO> rankedPairWinners = new ArrayList<>();
	
	public static RankedWinnerDTO build(final Ballot ballot, final BallotOption winner) {
		RankedWinnerDTO dto = new RankedWinnerDTO();
		
		if (ballot != null) { dto.setBallotId(ballot.getId()); }
		if (ballot != null) { dto.setWinner(BallotOptionDTO.build(winner)); }
		if (ballot != null) { ballot.getRankedPairWinners().stream().forEach(rpw -> dto.getRankedPairWinners().add(RankedPairWinnerDTO.build(rpw))); }
		
		return dto;
	}

	public Long getBallotId() {
		return ballotId;
	}

	public void setBallotId(Long ballotId) {
		this.ballotId = ballotId;
	}

	public BallotOptionDTO getWinner() {
		return winner;
	}

	public void setWinner(BallotOptionDTO winner) {
		this.winner = winner;
	}

	public List<RankedPairWinnerDTO> getRankedPairWinners() {
		return rankedPairWinners;
	}

	public void setRankedPairWinners(List<RankedPairWinnerDTO> rankedPairWinners) {
		this.rankedPairWinners = rankedPairWinners;
	}
}
