package org.psu.edu.sweng.capstone.backend.dto;

import java.util.ArrayList;
import java.util.List;

import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEntity<T> {

	private Boolean success;
	private int status;
	
	List<T> data = new ArrayList<>();
	List<ResponseError> errors = new ArrayList<>();
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public List<T> getData() {
		return data;
	}
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public List<ResponseError> getErrors() {
		return errors;
	}
	
	public void setErrors(List<ResponseError> errors) {
		this.errors = errors;
	}
	
	public void attachExceptionError(Exception e) {
		this.getErrors().add(new ResponseError(ErrorEnum.EXCEPTION_THROWN, e.getMessage()));
		
		this.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		this.setSuccess(false);	
	}
	
	public void attachGenericSuccess() {
		this.setSuccess(true);
		this.setStatus(HttpStatus.OK.value());
	}
	
	public String writeUserWasNotFoundMessage(String userName) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(userName).append(" was not found.");
		
		return builder.toString();
	}
}