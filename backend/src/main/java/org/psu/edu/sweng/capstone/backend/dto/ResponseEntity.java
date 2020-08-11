package org.psu.edu.sweng.capstone.backend.dto;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<ResponseError> getErrors() {
		return errors;
	}
		
	public void attachGenericSuccess() {
		this.setSuccess(true);
		this.setStatus(HttpStatus.OK.value());
	}
	
	public void attachCreatedSuccess() {
		this.setSuccess(true);
		this.setStatus(HttpStatus.CREATED.value());
	}
}
