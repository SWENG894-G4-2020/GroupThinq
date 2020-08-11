package org.psu.edu.sweng.capstone.backend.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

public class ErrorEnumTest {
	
	private static final EnumSet<ErrorEnum> ENUM_VALUES = EnumSet.allOf(ErrorEnum.class);

	@Test
	void containsAllEnums() {
		assertEquals(4, ENUM_VALUES.size());
		
		assertTrue(ENUM_VALUES.contains(ErrorEnum.ENTITY_NOT_FOUND));
		assertTrue(ENUM_VALUES.contains(ErrorEnum.EXCEPTION_THROWN));
		assertTrue(ENUM_VALUES.contains(ErrorEnum.RESOURCE_CONFLICT));
		assertTrue(ENUM_VALUES.contains(ErrorEnum.UNAUTHORIZED_ACTION));
	}
}
