package com.engro.paperlessbackend.dto;

import org.springframework.stereotype.Component;

@Component
public class AssetTemplateAdditionDto {
	
	private int templateId;
	private int assetTemplateId;
	
	public AssetTemplateAdditionDto() {}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getAssetTemplateId() {
		return assetTemplateId;
	}

	public void setAssetTemplateId(int assetTemplateId) {
		this.assetTemplateId = assetTemplateId;
	}
}
