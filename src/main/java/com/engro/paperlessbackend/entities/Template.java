package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tbl_template")
@EntityListeners(AuditingEntityListener.class)
public class Template {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id")
	private int id;
	
	@NotNull
	@Column(name = "template_name")
	private String name;
	
	@Column(name = "description")
	private String description;

//	@NotNull
//	@Column(name = "org_id_fk")
//	private int organizationId;
	
	public Template(){}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public int getOrganizationId() {
//		return organizationId;
//	}
//
//	public void setOrganizationId(int organizationId) {
//		this.organizationId = organizationId;
//	}
}
