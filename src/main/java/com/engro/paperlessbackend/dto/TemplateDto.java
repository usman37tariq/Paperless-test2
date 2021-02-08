package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.TemplateStructure;

public class TemplateDto {
	
	private int id;
	private String name;
	private String description;
	private List<TemplateStructure> templateStructures;
	
	public TemplateDto() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TemplateStructure> getTemplateStructures() {
		return templateStructures;
	}

	public void setTemplateStructures(List<TemplateStructure> templateStructures) {
		this.templateStructures = templateStructures;
	}
	
}
