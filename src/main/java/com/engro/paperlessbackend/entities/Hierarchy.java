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
@Table(name = "tbl_hierarchy")
@EntityListeners(AuditingEntityListener.class)
public class Hierarchy {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "node_id")
	private int id;
	
	@NotNull
	@Column(name = "node_parent_id")
	private int parent;
	
	@NotNull
	@Column(name = "node_name")
	private String name;
	
	@NotNull
	@Column(name = "node_active_status")
	private int activeStatus = 1;
	
	@Column(name = "node_description")
	private String description;
	
	@Column(name = "node_type")
	private String type;
	
	@Column(name = "sap_ref_num")
	private String sapRefNumber;
	
//	@NotNull
//	@Column(name = "org_id_fk")
//	private int organizationId;
	
	public Hierarchy() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getParent() {
		if (parent == -1) {
			return "#";
		}
		return String.valueOf(parent);
	}
	
	public void setParent(int parentId) {
		this.parent = parentId;
	}
	
	public int getActiveStatus() {
		return activeStatus;
	}
	
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSapRefNumber() {
		return sapRefNumber;
	}
	
	public void setSapRefNumber(String sapRefNumber) {
		this.sapRefNumber = sapRefNumber;
	}

//	public int getOrganizationId() {
//		return organizationId;
//	}
//
//	public void setOrganizationId(int organizationId) {
//		this.organizationId = organizationId;
//	}
}
