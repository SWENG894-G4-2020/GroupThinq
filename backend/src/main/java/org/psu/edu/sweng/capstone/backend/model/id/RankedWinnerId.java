package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;
import java.util.Objects;

public class RankedWinnerId implements Serializable {

	private static final long serialVersionUID = -6737147629086104329L;
	
	private Long ballot;
	private Long winner;

	public RankedWinnerId() {}
	
	public RankedWinnerId(Long ballot, Long winner) {
		this.ballot = ballot;
		this.winner = winner;
	}

	public Long getBallot() {
		return ballot;
	}

	public Long getWinner() {
		return winner;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getBallot(), this.getWinner());
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (!(o instanceof UserRoleId)) {
        	return false;
        }
        
        UserRoleId userRoleId = (UserRoleId) o;
        
        return (Objects.equals(this.getWinner(), userRoleId.getRole()) &&
        		Objects.equals(this.getBallot(), userRoleId.getUser()));
	}

}
