package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;

class ServiceImplTest {
	
	protected void assertGenericSuccess(ResponseEntity<?> response) {
		assertEquals(true, response.getSuccess());
		assertEquals(200, response.getStatus());
		assertEquals(0, response.getErrors().size());
	}
	
	protected void assertCreatedSuccess(ResponseEntity<?> response) {
		assertEquals(0, response.getErrors().size());
		assertEquals(201, response.getStatus());
		assertTrue(response.getSuccess());
	}
	
	@Test
	void assertGenericSuccess_returnsTrue() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		response.setStatus(200);
		response.setSuccess(true);
		
		assertGenericSuccess(response);
	}
	
	@Test
	void assertCreatedSuccess() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		response.setStatus(201);
		response.setSuccess(true);
		
		assertCreatedSuccess(response);
	}
}
