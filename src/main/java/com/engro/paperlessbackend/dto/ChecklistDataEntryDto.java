package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.entities.ChecklistSchedule;
import com.engro.paperlessbackend.entities.Users;

/**
 * when user submits a checklist after data entry
 * this dto will receive all the data entries,
 * user info and checklist info
 * 
 * @author Arslan Saddique
 *
 */
public class ChecklistDataEntryDto {

	private long timeStamp;
	private Checklist checklist;
	private Users user;
	private List<ChecklistFieldValueDto> checklistFields;
	private String checklistRemarks;
	private ChecklistSchedule checklistSchedule;
	
	public ChecklistDataEntryDto() {}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public List<ChecklistFieldValueDto> getChecklistFields() {
		return checklistFields;
	}

	public void setChecklistFields(List<ChecklistFieldValueDto> checklistFields) {
		this.checklistFields = checklistFields;
	}

	public String getChecklistRemarks() {
		return checklistRemarks;
	}

	public void setChecklistRemarks(String checklistRemarks) {
		this.checklistRemarks = checklistRemarks;
	}

	public ChecklistSchedule getChecklistSchedule() {
		return checklistSchedule;
	}

	public void setChecklistSchedule(ChecklistSchedule checklistSchedule) {
		this.checklistSchedule = checklistSchedule;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
