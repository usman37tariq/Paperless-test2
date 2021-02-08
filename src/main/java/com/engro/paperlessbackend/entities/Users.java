package com.engro.paperlessbackend.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.utils.Constants;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class Users {

	@Id
	@NotNull
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "user_id")
	private String id;

	@NotNull
	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;
	
	@Column(name = "password_updated_at")
	private Long passwordUpdatedAt;

	@NotNull
	@Column(name = "email")
	private String email;

	@NotNull
	@Column(name = "date")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date = LocalDate.now();

	@NotNull
	@Column(name = "division")
	private String division;

	@NotNull
	@Column(name = "designation")
	private String designation;

//	@ManyToOne(fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name = "department", nullable = true)
//	private Department department;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "section", nullable = true)
	private Section section;

	@Column(name = "phone_number")
	private Long phoneNumber;

	@Column(name = "position")
	private String position;

	@Column(name = "company")
	private String company;

	@Column(name = "role")
	private String role;

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "user_role", nullable = true)
	private Role userRole;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Column(name = "dashboard_order")
	private String dashboardOrder;

	@Column(name = "address")
	private String address;

	@NotNull
	@Column(name = "status")
	private String status = Constants.USER_STATUS_ACTIVE;

	@NotNull
	@Column(name = "date_added", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Timestamp dateAdded = new Timestamp(System.currentTimeMillis());

	@Column(name = "date_inactive", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Timestamp dateInactive;

	@Column(name = "language")
	private String language;

	public Users() {
		/**
		 * Empty Constructor
		 */
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

//	public Department getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(Department department) {
//		this.department = department;
//	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role role) {
		this.userRole = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getDashboardOrder() {
		return dashboardOrder;
	}

	public void setDashboardOrder(String dashboardOrder) {
		this.dashboardOrder = dashboardOrder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Timestamp getDateInactive() {
		return dateInactive;
	}

	public void setDateInactive(Timestamp dateInactive) {
		this.dateInactive = dateInactive;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getPasswordUpdatedAt() {
		return passwordUpdatedAt;
	}

	public void setPasswordUpdatedAt(Long passwordUpdatedAt) {
		this.passwordUpdatedAt = passwordUpdatedAt;
	}
}
