package com.engro.paperlessbackend.dto;

import com.engro.paperlessbackend.entities.TemplateStructure;

/**
 * this dto will have information of value entered
 * against a checklist field during data entry along 
 * with that field's remarks
 * @author Arslan Saddique
 *
 */
public class ChecklistFieldValueDto {
	
	private TemplateStructure templateStructure;
	private String value;
	private String fieldRemarks;
	
	public ChecklistFieldValueDto() {}

	public TemplateStructure getTemplateStructure() {
		return templateStructure;
	}

	public void setTemplateStructure(TemplateStructure templateStructure) {
		this.templateStructure = templateStructure;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldRemarks() {
		return fieldRemarks;
	}

	public void setFieldRemarks(String fieldRemarks) {
		this.fieldRemarks = fieldRemarks;
	}
}
