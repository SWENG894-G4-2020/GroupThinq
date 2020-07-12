package org.psu.edu.sweng.capstone.backend.exception;

public class BallotExpiredException extends Exception {

    private static final long serialVersionUID = -8675309333L;

    public BallotExpiredException(String entity) {
        super(entity + " is already expired, no further actions can be applied to it.");
    }

}
