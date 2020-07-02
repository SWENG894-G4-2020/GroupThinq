package org.psu.edu.sweng.capstone.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EntityNotFoundExceptionTest {

	@Test
	void constructor_worksProperly() {
		EntityNotFoundException e = new EntityNotFoundException("Test Entity");
		assertEquals("Test Entity could not be found", e.getMessage());
	}
}
