package org.psu.edu.sweng.capstone.backend.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniquePairTest {
	
	private UniquePair up;
	
	@BeforeEach
	void setUp() { up = new UniquePair(1L, 2L); }
	
	@Test
	void getters_workProperly() {
		assertEquals(1L, up.getOptionOne());
		assertEquals(2L, up.getOptionTwo());
	}
	
	@Test
	void setters_workProperly() {
		up.setOptionOne(3L);
		up.setOptionTwo(4L);
		
		assertEquals(3L, up.getOptionOne());
		assertEquals(4L, up.getOptionTwo());
	}
	
	@Test
	void constructor_worksProperly() {
		final UniquePair up = new UniquePair(5L, 6L);
		
		assertEquals(5L, up.getOptionOne());
		assertEquals(6L, up.getOptionTwo());
	}
	
	@Test
	void toString_worksProperly() {
		assertEquals("[1, 2]", up.toString());
	}
}
