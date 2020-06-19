package org.psu.edu.sweng.capstone.backend.dto;

import org.psu.edu.sweng.capstone.backend.enumeration.ErrorEnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseError {

	private ErrorEnum type;
	private String message;
	
	public ResponseError(ErrorEnum type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public ErrorEnum getType() {
		return type;
	}
	
	public void setType(ErrorEnum type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Response Error: ").append("\n")
		.append("Type: ").append(this.getType().toString()).append("\n")
		.append("Message: ").append(this.getMessage());
		
		return builder.toString();
	}
}
