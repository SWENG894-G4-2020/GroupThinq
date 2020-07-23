package org.psu.edu.sweng.capstone.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class BallotTypeTest {
	
	private static final BallotType TEST_TYPE = new BallotType(1L, "Single-Choice");
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, TEST_TYPE.getId());
		assertEquals("Single-Choice", TEST_TYPE.getName());
	}
	
	@Test
	void constructor_worksProperly() {
		BallotType newType = new BallotType(2L, "Ranked-Choice");
		
		assertEquals(2L, newType.getId());
		assertEquals("Ranked-Choice", newType.getName());
	}
	
	@Test
	void defaultConstructor_worksProperly() {
		BallotType newType = new BallotType();
		
		assertNull(newType.getId());
		assertNull(newType.getName());
	}
}
