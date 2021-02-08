package com.engro.paperlessbackend.dto;

import com.engro.paperlessbackend.entities.ChecklistStatusHistory;

public class ChecklistApproveDto {
	
	private ChecklistStatusHistory checklistApprove;
	private int isApproved;
	
	public ChecklistApproveDto() {}

	public ChecklistStatusHistory getChecklistApprove() {
		return checklistApprove;
	}

	public void setChecklistApprove(ChecklistStatusHistory checklistApprove) {
		this.checklistApprove = checklistApprove;
	}

	public int getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(int isApproved) {
		this.isApproved = isApproved;
	}
}
