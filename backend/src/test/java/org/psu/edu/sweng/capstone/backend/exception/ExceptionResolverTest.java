package org.psu.edu.sweng.capstone.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

class ExceptionResolverTest {
	
	private ExceptionResolver resolver = new ExceptionResolver();
	
	@Test
	void handleEntityConflict_properlySetsResponse() {
		// given
		EntityConflictException e = new EntityConflictException("Test Entity");
		
		// when
		ResponseEntity<String> response = resolver.handleEntityConflict(e);
		
		// then
		assertFalse(response.getSuccess());
		
		assertEquals(0, response.getData().size());
		assertEquals(1, response.getErrors().size());
		assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
		assertEquals(ErrorEnum.RESOURCE_CONFLICT, response.getErrors().get(0).getType());
	}
	
	@Test
	void handleEntityNotFound_properlySetsResponse() {
		// given
		EntityNotFoundException e = new EntityNotFoundException("Test Entity");
		
		// when
		ResponseEntity<String> response = resolver.handleEntityNotFound(e);
		
		// then
		assertFalse(response.getSuccess());
		
		assertEquals(0, response.getData().size());
		assertEquals(1, response.getErrors().size());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		assertEquals(ErrorEnum.ENTITY_NOT_FOUND, response.getErrors().get(0).getType());
	}
	
	@Test
	void handleSystemException_properlySetsResponse() {
		// given
		Exception e = new Exception("ERROR");
		
		// when
		ResponseEntity<String> response = resolver.handleSystemException(e);
		
		// then
		assertFalse(response.getSuccess());
		
		assertEquals(0, response.getData().size());
		assertEquals(1, response.getErrors().size());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
		assertEquals(ErrorEnum.EXCEPTION_THROWN, response.getErrors().get(0).getType());
	}
	
	@Test
	void handleAccessDeniedException_properlySetsResponse() {
		// given
		AccessDeniedException e = new AccessDeniedException("ERROR");
		
		// when
		ResponseEntity<String> response = resolver.handleUnauthorizedAccess(e);
		
		// then
		assertFalse(response.getSuccess());
		
		assertEquals(0, response.getData().size());
		assertEquals(1, response.getErrors().size());
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
		assertEquals(ErrorEnum.UNAUTHORIZED_ACTION, response.getErrors().get(0).getType());
	}
}
