package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE_REF")
public class Role {

	@Id
	private Long id;

	@Column(name = "NAME")
	private String name;
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
