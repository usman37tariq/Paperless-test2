package com.engro.paperlessbackend.dto;

public class ChecklistStructureDto {

	private int checklistFieldId;
	private int checklistOrderId;
	private String description;
	private String fieldType;
	private Double lowerLimit;
	private Double upperLimit;
	private String unitOfMeasure;
	private int templateId;
	private int templateFieldId;
	
	private Object value;
	private String remarks;
	
	public ChecklistStructureDto() {}

	public int getChecklistFieldId() {
		return checklistFieldId;
	}

	public void setChecklistFieldId(int checklistFieldId) {
		this.checklistFieldId = checklistFieldId;
	}

	public int getChecklistOrderId() {
		return checklistOrderId;
	}

	public void setChecklistOrderId(int checklistOrderId) {
		this.checklistOrderId = checklistOrderId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Double getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getTemplateFieldId() {
		return templateFieldId;
	}

	public void setTemplateFieldId(int templateFieldId) {
		this.templateFieldId = templateFieldId;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
}
