package com.engro.paperlessbackend.dto;

public class GetAssetTemplateStructureDto {

	private int nodeId;
	private String templateName;
	
	public GetAssetTemplateStructureDto() {}
	
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	
}
