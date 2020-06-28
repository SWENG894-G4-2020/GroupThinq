package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.ResponseError;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;

class ServiceImplTest {
	
	protected void assertExceptionThrown(ResponseEntity<?> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(500, response.getStatus());
		assertEquals(false, response.getSuccess());
	}

	protected void assertGenericSuccess(ResponseEntity<UserDTO> response) {
		assertEquals(true, response.getSuccess());
		assertEquals(200, response.getStatus());
		assertEquals(0, response.getErrors().size());
	}
	
	protected void assertResourceConflictIssues(ResponseEntity<UserDTO> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(404, response.getStatus());
		assertEquals(false, response.getSuccess());
	}
	
	@Test
	void assertExceptionThrown_returnsTrue() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		response.getErrors().add(new ResponseError(ErrorEnum.EXCEPTION_THROWN, "Error"));
		response.setStatus(500);
		response.setSuccess(false);
		
		assertExceptionThrown(response);
	}
	
	@Test
	void assertGenericSuccess_returnsTrue() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		response.setStatus(200);
		response.setSuccess(true);
		
		assertGenericSuccess(response);
	}
	
	@Test
	void assertResourceConflictIssues() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();
		
		response.getErrors().add(new ResponseError(ErrorEnum.RESOURCE_CONFLICT, "Resource Conflict"));
		response.setStatus(404);
		response.setSuccess(false);
		
		assertResourceConflictIssues(response);
	}
}
