package org.psu.edu.sweng.capstone.backend.enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

class RoleEnumTest {

	private static final List<Object> ENUM_VALUES = Arrays.asList(RoleEnum.values());
	
	@Test
	void containsAllEnums() {
		assertEquals(2, ENUM_VALUES.size());
		
		assertTrue(ENUM_VALUES.contains(RoleEnum.USER));
		assertTrue(ENUM_VALUES.contains(RoleEnum.ADMIN));
	}
}
