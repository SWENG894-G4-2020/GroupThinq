package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BALLOT_TYPE_REF")
public class BallotType {

	@Id
	private Long id;
	
	@Column(name = "NAME")
	private String name;

	public BallotType() {}
	
	public BallotType(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
