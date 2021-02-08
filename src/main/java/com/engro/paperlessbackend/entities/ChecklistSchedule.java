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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "tbl_checklist_schedule")
@Table(name = "tbl_checklist_schedule", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "checklist_id_fk", "start_timestamp", "end_timestamp" }) })
@EntityListeners(AuditingEntityListener.class)
public class ChecklistSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_schedule_id")
	int checklistScheduleId;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_id_fk")
	Checklist checklist;

	@NotNull
	@Column(name = "start_timestamp")
	Long startTimestamp;

	@Column(name = "end_timestamp")
	Long endTimestamp;

	@Column(name = "frequency", columnDefinition="integer default 0")
	int frequency;

	@Column(name = "unit")
	String unit;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "checklist_state_id_fk")
	ChecklistState checklistState;

	public int getChecklistScheduleId() {
		return checklistScheduleId;
	}

	public void setChecklistScheduleId(int checklistScheduleId) {
		this.checklistScheduleId = checklistScheduleId;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public Long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Long endTimestamp) {
		this.endTimestamp = endTimestamp;
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
	
	public ChecklistState getChecklistState() {
		return checklistState;
	}

	public void setChecklistState(ChecklistState checklistState) {
		this.checklistState = checklistState;
	}

	@JsonIgnore
	public boolean isManual() {
		if(this.unit.equalsIgnoreCase(Constants.MANUAL_CHECKLIST_UNIT) || this.frequency == 0) {
			return true;
		}
		return false;
	}
}
