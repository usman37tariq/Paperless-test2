package com.engro.paperlessbackend.entities;

import java.sql.Timestamp;

/**
 * Contains attributes for Tag Entity.
 * 
 * @author Ehsan Waris
 *
 */
public class Tag {
	int id;
	Timestamp timestamp;
	int quality;
	int checklistId;
	int checklistScheduleId;
	String userId;
	String remarks;
	Object value;
	Timestamp dataEntryTimestamp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(int checklistId) {
		this.checklistId = checklistId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Timestamp getDataEntryTimestamp() {
		return dataEntryTimestamp;
	}

	public void setDataEntryTimestamp(Timestamp dateEntryTimestamp) {
		this.dataEntryTimestamp = dateEntryTimestamp;
	}

	public int getChecklistScheduleId() {
		return checklistScheduleId;
	}

	public void setChecklistScheduleId(int checklistScheduleId) {
		this.checklistScheduleId = checklistScheduleId;
	}
	
	
}
