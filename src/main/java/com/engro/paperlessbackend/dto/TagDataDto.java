package com.engro.paperlessbackend.dto;

import java.sql.Timestamp;

/**
 * tag data extracted from each field will be mapped to this dto and this will
 * be inserted into the corresponding tag table
 * 
 * @author Arslan Saddique
 *
 */
public class TagDataDto {

	private Timestamp timeStamp;
	private int quality;
	private String value;
	private String remarks;
	private String userId;
	private int checklistId;
	private int checklistScheduleId;
	private Timestamp dataTimestamp;

	public TagDataDto() {
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(int checklistId) {
		this.checklistId = checklistId;
	}

	public int getChecklistScheduleId() {
		return checklistScheduleId;
	}

	public void setChecklistScheduleId(int checklistScheduleId) {
		this.checklistScheduleId = checklistScheduleId;
	}

	public Timestamp getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(Timestamp dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}
}
