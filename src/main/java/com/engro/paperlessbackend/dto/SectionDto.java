package com.engro.paperlessbackend.dto;

public class SectionDto {

	private int sectionId;
	private String sectionName;
	
	public SectionDto() {}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}
