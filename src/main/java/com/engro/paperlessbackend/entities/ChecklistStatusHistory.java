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
@Table(name = "tbl_checklist_status_history")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistStatusHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "checklist_history_id")
	int checklistHistoryId;

	@NotNull
	@Column(name = "status_timestamp")
	long statusTimestamp;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_schedule_id")
	ChecklistSchedule checklistSchedule;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id_fk")
	Users user;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "workflow_level_id_fk")
	WorkflowLevel workflowLevel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "previous_workflow_level_id_fk")
	WorkflowLevel previousWorkflowLevel;

	@Column(name = "remarks")
	String remarks;

	public int getChecklistHistoryId() {
		return checklistHistoryId;
	}

	public void setChecklistHistoryId(int checklistHistoryId) {
		this.checklistHistoryId = checklistHistoryId;
	}

	public long getStatusTimestamp() {
		return statusTimestamp;
	}

	public void setStatusTimestamp(long statusTimestamp) {
		this.statusTimestamp = statusTimestamp;
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

	public WorkflowLevel getWorkflowLevel() {
		return workflowLevel;
	}

	public void setWorkflowLevel(WorkflowLevel workflowLevel) {
		this.workflowLevel = workflowLevel;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public WorkflowLevel getPreviousWorkflowLevel() {
		return previousWorkflowLevel;
	}

	public void setPreviousWorkflowLevel(WorkflowLevel previousWorkflowLevel) {
		this.previousWorkflowLevel = previousWorkflowLevel;
	}

}
