package com.engro.paperlessbackend.dto;

import com.engro.paperlessbackend.entities.ChecklistSchedule;

public class ChecklistScheduleStatusDto {

	private ChecklistSchedule checklistSchedule;
	private String status;
	private int isRejected;
	private String assignedTo;
	private long dueTime;
	private int isManual;
	
	public ChecklistScheduleStatusDto() {}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(int isRejected) {
		this.isRejected = isRejected;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public long getDueTime() {
		return dueTime;
	}

	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}

	public int getIsManual() {
		return isManual;
	}

	public void setIsManual(int isManual) {
		this.isManual = isManual;
	}
}
