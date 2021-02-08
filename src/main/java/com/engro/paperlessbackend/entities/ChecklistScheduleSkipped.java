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

@Entity
@Table(name = "tbl_checklist_schedule_Skipped")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistScheduleSkipped {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "checklist_schedule_skipped_id")
	int checklistSchedlueSkippedId;

	@NotNull
	@Column(name = "skipped_timestamp")
	long skippedTimestamp;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_schedule_id_fk")
	ChecklistSchedule checklistSchedule;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id_fk")
	Users user;

	@NotNull
	@Column(name = "remarks")
	String remarks;

	public ChecklistScheduleSkipped() {}
	
	public int getChecklistSchedlueSkippedId() {
		return checklistSchedlueSkippedId;
	}

	public void setChecklistSchedlueSkippedId(int checklistSchedlueSkippedId) {
		this.checklistSchedlueSkippedId = checklistSchedlueSkippedId;
	}

	public long getSkippedTimestamp() {
		return skippedTimestamp;
	}

	public void setSkippedTimestamp(long skippedTimestamp) {
		this.skippedTimestamp = skippedTimestamp;
	}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
