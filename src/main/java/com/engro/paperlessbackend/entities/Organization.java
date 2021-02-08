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

/**
 * Organization Entity Used in both Iridium/SSA and Paperless
 * 
 * @author Ehsan Waris
 *
 */

@Entity
@Table(name = "organization")
@EntityListeners(AuditingEntityListener.class)
public class Organization {

	
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Id
	@NotNull
	@Column(name = "organization_id")
	private String organizationId;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "email")
	private String email;

	@Column(name = "business_type")
	private String businessType;

	@Column(name = "role")
	private String role;

	@Column(name = "phone")
	private String phone;

	@Column(name = "package")
	private String orgnaizationPackage;

	@Column(name = "status")
	private String status;

	@Column(name = "number_of_users")
	private Integer numberOfUsers;

	@Column(name = "number_of_tags")
	private Integer numberOfTags;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrgnaizationPackage() {
		return orgnaizationPackage;
	}

	public void setOrgnaizationPackage(String orgnaizationPackage) {
		this.orgnaizationPackage = orgnaizationPackage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(Integer numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public Integer getNumberOfTags() {
		return numberOfTags;
	}

	public void setNumberOfTags(Integer numberOfTags) {
		this.numberOfTags = numberOfTags;
	}

}
