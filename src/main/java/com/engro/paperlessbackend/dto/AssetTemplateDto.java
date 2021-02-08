package com.engro.paperlessbackend.dto;

public class AssetTemplateDto {

	private String name;
	private String description;
	private int nodeId;
	
	public AssetTemplateDto() {}
	
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

	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
}
