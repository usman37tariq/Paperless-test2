package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name= "login_history")
public class LoginHistory {
	@Id
	@NotNull
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "user_id")
	private String id;
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "user_role", nullable = true)
	private IPs ip;
}
