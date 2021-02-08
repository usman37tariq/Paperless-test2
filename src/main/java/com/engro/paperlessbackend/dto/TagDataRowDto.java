package com.engro.paperlessbackend.dto;

public class TagDataRowDto {

	long timestamp;
	Object value;
	
	Object [] dataArray = new Object[] {timestamp, value};
	public TagDataRowDto() {
	}
	public TagDataRowDto(long timestamp, double value) {
		this.timestamp = timestamp;
		this.value = value;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object object) {
		this.value = object;
	}
	
}
