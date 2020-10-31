package com.example.demomeliguslaw.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Job implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -6545913013346366159L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String role;
	private Long salary;

	public Job() {
	}

	public Job(String role, Long salary) {
		super();
		this.role = role;
		this.salary = salary;
	}
}
