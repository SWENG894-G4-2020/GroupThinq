package org.psu.edu.sweng.capstone.backend.dto;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ResponseEntityTest {

	private final ResponseEntity<?> re = new ResponseEntity<>();
	
	@BeforeEach
	void setUp() {
		re.setStatus(200);
		re.setSuccess(true);
	}
		
	@Test
	void getters_workProperly() {
		assertEquals(0, re.getData().size());
		assertEquals(0, re.getErrors().size());
		assertEquals(200, re.getStatus());
		assertEquals(true, re.getSuccess());
	}
	
	@Test
	void setters_workProperly() {
		// when
		re.setStatus(400);
		re.setSuccess(false);
		
		// then
		assertEquals(400, re.getStatus());
		assertEquals(false, re.getSuccess());
	}
	
	@Test
	void writeEntityNotFound_writesAppropriateMessage() {
		assertEquals("testuser was not found.", re.writeEntityNotFound("testuser"));
	}
	
	@Test
	void attachGenericSuccess_setsAppropriateValues() {
		// given
		re.setStatus(400);
		re.setSuccess(false);
		
		// when
		re.attachGenericSuccess();
		
		// then
		assertEquals(200, re.getStatus());
		assertEquals(true, re.getSuccess());
	}
	
	@Test
	void attachExceptionError_setsAppropriateValues() {
		// given
		assertEquals(0, re.getErrors().size());
		
		// when
		re.attachExceptionError(new Exception("Exception"));
		
		// then
		assertEquals(1, re.getErrors().size());
		assertEquals(false, re.getSuccess());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), re.getStatus());
	}
}
