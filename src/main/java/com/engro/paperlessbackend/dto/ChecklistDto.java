package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistApproverGroup;
import com.engro.paperlessbackend.entities.ChecklistApproverUser;

public class ChecklistDto {

	private Checklist checklist;
	private List<ChecklistApproverGroup> approverGroups;
	private List<ChecklistApproverUser> approverUsers;

	public ChecklistDto() {
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public List<ChecklistApproverGroup> getApproverGroups() {
		return approverGroups;
	}

	public void setApproverGroups(List<ChecklistApproverGroup> approverGroups) {
		this.approverGroups = approverGroups;
	}

	public List<ChecklistApproverUser> getApproverUsers() {
		return approverUsers;
	}

	public void setApproverUsers(List<ChecklistApproverUser> approverUsers) {
		this.approverUsers = approverUsers;
	}

}
