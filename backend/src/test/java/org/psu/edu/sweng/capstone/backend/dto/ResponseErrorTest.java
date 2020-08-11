package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;

class ResponseErrorTest {

	private final ResponseError re = new ResponseError(ErrorEnum.RESOURCE_CONFLICT, "Test Error Message");
	
	@Test
	void constructor_buildsProperly() {
		// when
		ResponseError newRE = new ResponseError(ErrorEnum.UNAUTHORIZED_ACTION, "Constructor Error Message");
		
		// then
		assertEquals(ErrorEnum.UNAUTHORIZED_ACTION, newRE.getType());
		assertEquals("Constructor Error Message", newRE.getMessage());
	}
	
	@Test
	void getters_workProperly() {
		assertEquals(ErrorEnum.RESOURCE_CONFLICT, re.getType());
		assertEquals("Test Error Message", re.getMessage());
	}
	
	@Test
	void setters_workProperly() {
		// when
		re.setType(ErrorEnum.EXCEPTION_THROWN);
		re.setMessage("New Error Message");
		
		// then
		assertEquals(ErrorEnum.EXCEPTION_THROWN, re.getType());
		assertEquals("New Error Message", re.getMessage());
	}
	
	@Test
	void toString_returnsProperValues() {
		// when
		String expectedToString = "Response Error: \n" + "Type: " + ErrorEnum.RESOURCE_CONFLICT.toString() + "\n" +
		"Message: " + re.getMessage();
		
		// then
		assertEquals(expectedToString, re.toString());
	}
}
