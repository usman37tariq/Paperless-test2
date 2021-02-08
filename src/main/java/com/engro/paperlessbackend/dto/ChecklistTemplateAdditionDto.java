package com.engro.paperlessbackend.dto;

public class ChecklistTemplateAdditionDto {

	private int checklistId;
	private int nodeId;
	
	public ChecklistTemplateAdditionDto() {}

	public int getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(int checklistId) {
		this.checklistId = checklistId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
}
