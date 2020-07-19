package org.psu.edu.sweng.capstone.backend.model.id;

import java.io.Serializable;
import java.util.Objects;

public class BallotResultId implements Serializable {

	private static final long serialVersionUID = -6257895573922018398L;

	private Long ballot;
	private Long ballotOption;
	private Long user;
	
	public BallotResultId() {}
	
	public BallotResultId(Long ballot, Long ballotOption, Long user) {
		this.ballot = ballot;
		this.ballotOption = ballotOption;
		this.user = user;
	}
	
	public Long getBallot() {
		return ballot;
	}
	
	public Long getBallotOption() {
		return ballotOption;
	}
	
	public Long getUser() {
		return user;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getBallot(), this.getBallotOption(), this.getUser());
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (!(o instanceof BallotResultId)) {
        	return false;
        }
        
        BallotResultId ballotResultId = (BallotResultId) o;
        
        return (Objects.equals(this.getBallot(), ballotResultId.getBallot()) &&
        		Objects.equals(this.getBallotOption(), ballotResultId.getBallotOption()) &&
				Objects.equals(this.getUser(), ballotResultId.getUser()));
	}
}
