package org.psu.edu.sweng.capstone.backend.model;

import java.util.LinkedList;

public class BallotVote {

    private String username;
    private LinkedList<Long> ballotOptionIds;

    public BallotVote(String username, LinkedList<Long> ballotOptionIds){
        this.username = username;
        this.ballotOptionIds = ballotOptionIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LinkedList<Long> getBallotOptionIds() {
        return ballotOptionIds;
    }

    public void setBallotOptionIds(LinkedList<Long> ballotOptionIds) {
        this.ballotOptionIds = ballotOptionIds;
    }
}
