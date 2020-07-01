package org.psu.edu.sweng.capstone.backend.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.ResponseError;
import org.psu.edu.sweng.capstone.backend.dto.UserDTO;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

class ServiceImplTest {
	
    @Mock
    protected Appender<ILoggingEvent> mockAppender;

    @Captor
    protected ArgumentCaptor<LoggingEvent> captorLoggingEvent;
        
	protected void assertExceptionThrown(ResponseEntity<?> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(500, response.getStatus());
		assertEquals(false, response.getSuccess());
	}

	protected void assertGenericSuccess(ResponseEntity<?> response) {
		assertEquals(true, response.getSuccess());
		assertEquals(200, response.getStatus());
		assertEquals(0, response.getErrors().size());
	}
	
	protected void assertResourceConflictIssues(ResponseEntity<?> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(404, response.getStatus());
		assertEquals(false, response.getSuccess());
	}
	
	protected void assertCreatedSuccess(ResponseEntity<?> response) {
		assertEquals(0, response.getErrors().size());
		assertEquals(201, response.getStatus());
		assertTrue(response.getSuccess());
	}
	
	protected void assertEntityNotFound(ResponseEntity<?> response) {
		assertEquals(1, response.getErrors().size());
		assertEquals(404, response.getStatus());
		assertFalse(response.getSuccess());
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
	
	@Test
	void assertCreatedSuccess() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		response.setStatus(201);
		response.setSuccess(true);
		
		assertCreatedSuccess(response);
	}
	
	@Test
	void assertEntityNotFound() {
		ResponseEntity<UserDTO> response = new ResponseEntity<>();

		response.getErrors().add(new ResponseError(ErrorEnum.RESOURCE_CONFLICT, "Can't find entity"));
		response.setStatus(404);
		response.setSuccess(false);
		
		assertEntityNotFound(response);
	}
	
	protected void assertLoggingOccurredOnException(Appender<ILoggingEvent> mockAppender, ArgumentCaptor<LoggingEvent> captorLoggingEvent) {
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        
        assertNotNull(loggingEvent);
	}
}
