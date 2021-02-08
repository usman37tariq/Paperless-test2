package com.engro.paperlessbackend.dto;

/**
 *
 * for storing information of user approval screen
 * all the checklists to be approved by a user
 * 
 */

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.Users;

public class UserChecklistApproveDto {
	
	private ChecklistSchedule checklistSchedule;
	private Users submittedBy;
	private long submittedOn;
	private int isRejected;
	private int isSkipped;
	private int isManual;
	
	public UserChecklistApproveDto() {}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public Users getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(Users submittedBy) {
		this.submittedBy = submittedBy;
	}

	public long getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(long submittedOn) {
		this.submittedOn = submittedOn;
	}

	public int getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(int isRejected) {
		this.isRejected = isRejected;
	}

	public int getIsSkipped() {
		return isSkipped;
	}

	public void setIsSkipped(int isSkipped) {
		this.isSkipped = isSkipped;
	}

	public int getIsManual() {
		return isManual;
	}

	public void setIsManual(int isManual) {
		this.isManual = isManual;
	}
}
