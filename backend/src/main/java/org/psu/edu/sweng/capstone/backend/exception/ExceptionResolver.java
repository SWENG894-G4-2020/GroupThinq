package org.psu.edu.sweng.capstone.backend.exception;

import org.psu.edu.sweng.capstone.backend.dto.ResponseEntity;
import org.psu.edu.sweng.capstone.backend.dto.ResponseError;
import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionResolver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);
	
	@ExceptionHandler(value = EntityConflictException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	public @ResponseBody ResponseEntity<String> handleEntityConflict(final EntityConflictException e) {
		ResponseEntity<String> response = new ResponseEntity<>();
		
		LOGGER.error(e.getMessage(), e);

		response.setSuccess(false);
		response.setStatus(HttpStatus.CONFLICT.value());
		response.getErrors().add(new ResponseError(ErrorEnum.RESOURCE_CONFLICT, e.getMessage()));
		
		return response;
	}
	
	@ExceptionHandler(value = EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ResponseEntity<String> handleEntityNotFound(final EntityNotFoundException e) {
		ResponseEntity<String> response = new ResponseEntity<>();
		
		LOGGER.error(e.getMessage(), e);
		
		response.setSuccess(false);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.getErrors().add(new ResponseError(ErrorEnum.ENTITY_NOT_FOUND, e.getMessage()));
		
		return response;
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ResponseEntity<String> handleUnauthorizedAccess(final AccessDeniedException e) {
		ResponseEntity<String> response = new ResponseEntity<>();
		
		LOGGER.error(e.getMessage(), e);
		
		response.setSuccess(false);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.getErrors().add(new ResponseError(ErrorEnum.UNAUTHORIZED_ACTION, e.getMessage()));
		
		return response;
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ResponseEntity<String> handleSystemException(final Exception e) {		
		ResponseEntity<String> response = new ResponseEntity<>(); 
		
		LOGGER.error("Exception thrown:", e);
		
		response.setSuccess(false);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.getErrors().add(new ResponseError(ErrorEnum.EXCEPTION_THROWN, e.getMessage()));
		
		return response;
	}
}
