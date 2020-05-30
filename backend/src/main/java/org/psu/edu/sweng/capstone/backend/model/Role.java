package org.psu.edu.sweng.capstone.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.psu.edu.sweng.capstone.backend.enumeration.RoleEnum;

@Entity
@Table(name = "ROLE")
public class Role {

	@Id
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "NAME")
	private RoleEnum name;
	
	public Long getId() {
		return id;
	}
	
	public RoleEnum getName() {
		return name;
	}
}
