package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.ChecklistStatusHistory;
import com.engro.paperlessbackend.entities.WorkflowLevel;

public class GetChecklistScheduleDto extends GetChecklistStructureDto {

	ChecklistSchedule checklistSchedule;

	ChecklistStatusHistory dataEntryUser;
	List<ChecklistStatusHistory> approvers;
	ChecklistStatusHistory firstApprover;
	ChecklistStatusHistory secondApprover;

	WorkflowLevel currentWorkflowLevel;
	String remarks;

	String reasonForSkipping;

	List<ChecklistStatusHistory> checklistScheduleStatusHistory;
	
	boolean hasNext = true;
	boolean hasPrevious = true;
	
	long dataEntryTimestamp;
	
	

	public long getDataEntryTimestamp() {
		return dataEntryTimestamp;
	}

	public void setDataEntryTimestamp(long dataEntryTimestamp) {
		this.dataEntryTimestamp = dataEntryTimestamp;
	}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public WorkflowLevel getWorkflowLevel() {
		return currentWorkflowLevel;
	}

	public void setWorkflowLevel(WorkflowLevel workflowLevel) {
		this.currentWorkflowLevel = workflowLevel;
	}

	public ChecklistStatusHistory getDataEntryUser() {
		return dataEntryUser;
	}

	public void setDataEntryUser(ChecklistStatusHistory dataEntryUser) {
		this.dataEntryUser = dataEntryUser;
	}

	public List<ChecklistStatusHistory> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<ChecklistStatusHistory> approvers) {
		this.approvers = approvers;
	}
/*
	public ChecklistStatusHistory getSecondApprover() {
		return secondApprover;
	}

	public void setSecondApprover(ChecklistStatusHistory secondApprover) {
		this.secondApprover = secondApprover;
	}
*/
	public List<ChecklistStatusHistory> getChecklistScheduleStatusHistory() {
		return checklistScheduleStatusHistory;
	}

	public void setChecklistScheduleStatusHistory(List<ChecklistStatusHistory> checklistScheduleStatusHistoy) {
		this.checklistScheduleStatusHistory = checklistScheduleStatusHistoy;
	}

	public WorkflowLevel getCurrentWorkflowLevel() {
		return currentWorkflowLevel;
	}

	public void setCurrentWorkflowLevel(WorkflowLevel currentWorkflowLevel) {
		this.currentWorkflowLevel = currentWorkflowLevel;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReasonForSkipping() {
		return reasonForSkipping;
	}

	public void setReasonForSkipping(String reasonForSkipping) {
		this.reasonForSkipping = reasonForSkipping;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

}
