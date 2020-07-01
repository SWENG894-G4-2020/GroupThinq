package org.psu.edu.sweng.capstone.backend.exception;


public class EntityConflictException extends Exception {

	private static final long serialVersionUID = -7396676115136447556L;

	public EntityConflictException(String entity) {
		super(entity + " already exists");
	}
}
