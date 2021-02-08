package com.engro.paperlessbackend.dto;

import java.util.List;

public class ChecklistStructureGroupingDto {

	private String name;
	private List<ChecklistStructureDto> structure;
	
	public ChecklistStructureGroupingDto() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChecklistStructureDto> getStructure() {
		return structure;
	}

	public void setStructure(List<ChecklistStructureDto> structure) {
		this.structure = structure;
	}
}
