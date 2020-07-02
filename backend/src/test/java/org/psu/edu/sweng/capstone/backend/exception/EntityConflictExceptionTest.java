package org.psu.edu.sweng.capstone.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EntityConflictExceptionTest {
	
	@Test
	void constructor_worksProperly() {
		EntityConflictException e = new EntityConflictException("Test Entity");
		assertEquals("Test Entity already exists", e.getMessage());
	}
}
