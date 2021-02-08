package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_checklist")
@EntityListeners(AuditingEntityListener.class)
public class Checklist {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_id")
	private int checklistId;

	@NotNull
	@Column(name = "checklist_name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "start_time")
	private long startTime;

	@Column(name = "end_time")
	private long endTime;

	@Column(name = "frequency")
	private int frequency;

	@Column(name = "unit")
	private String unit;

//	@NotNull
//	@ManyToOne(fetch = FetchType.EAGER, optional = true)
//	@JoinColumn(name = "department_id_fk", nullable = true)
//	private Department department;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "section_id_fk", nullable = true)
	private Section section;

	@NotNull
	@Column(name = "activation_status")
	private String activationStatus;

	@NotNull
	@Column(name = "is_deleted")
	private int isDeleted;

	@Column(name = "created_at", columnDefinition = "bigint default (extract(epoch from now())::bigint) * 1000", insertable = false)
	private Long createdAt;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "created_by", nullable = true)
	private Users createdBy;

	public Checklist() {
	}

	public int getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(int id) {
		this.checklistId = id;
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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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

	public String getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	@JsonIgnore
	public boolean isManual() {
		if (this.unit.equalsIgnoreCase(Constants.MANUAL_CHECKLIST_UNIT) || this.frequency == 0) {
			return true;
		}
		return false;
	}
}
