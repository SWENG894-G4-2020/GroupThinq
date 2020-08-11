package org.psu.edu.sweng.capstone.backend.exception;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -3989723657493155029L;

	public EntityNotFoundException(String entity) {
		super(entity + " could not be found");
	}
}
