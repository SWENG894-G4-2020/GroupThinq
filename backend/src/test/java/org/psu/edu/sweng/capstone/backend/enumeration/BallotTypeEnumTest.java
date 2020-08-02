package org.psu.edu.sweng.capstone.backend.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

class BallotTypeEnumTest {
	
	private static final EnumSet<BallotTypeEnum> ENUM_VALUES = EnumSet.allOf(BallotTypeEnum.class);

	@Test
	void containsAllEnums() {
		assertEquals(2, ENUM_VALUES.size());
		
		assertTrue(ENUM_VALUES.contains(BallotTypeEnum.SINGLE_CHOICE));
		assertTrue(ENUM_VALUES.contains(BallotTypeEnum.RANKED_PAIR));
	}
	
	@Test
	void getDescription_worksProperly() {
		assertEquals("Ranked-Pair", BallotTypeEnum.RANKED_PAIR.getDescription());
	}
}
