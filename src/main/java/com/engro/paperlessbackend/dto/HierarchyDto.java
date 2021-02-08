package com.engro.paperlessbackend.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HierarchyDto {

	@JsonProperty("text")
	private String name;
	private String id;	
	private String parent;
	private String icon;
	private String state;
	private String description;
	private String sapRefNumber;
	private String type;
	private String unitOfMeasure;
	
	private Map<String, String> a_attr;

	public HierarchyDto() {
		a_attr = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		a_attr.put("id", this.id);
	}

	public String getParent() {
		if (parent.equals("-1")) {
			return "#";
		}
		return parent;
	}

	public void setParent(String parentId) {
		this.parent = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		if (icon == null) {
			return "";
		}
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getState() {
		if (state == null) {
			return "";
		}
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		a_attr.put("title", this.description);
	}

	public String getSapRefNumber() {
		return sapRefNumber;
	}

	public void setSapRefNumber(String sapRefNumber) {
		this.sapRefNumber = sapRefNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		a_attr.put("data-type", this.type);
	}

	public Map<String, String> getA_attr() {
		return a_attr;
	}

	public void setA_attr(Map<String, String> a_attr) {
		this.a_attr = a_attr;
	}

	public String getUoM() {
		return unitOfMeasure;
	}

	public void setUoM(String uoM) {
		this.unitOfMeasure = uoM;
		a_attr.put("uom", this.unitOfMeasure);
	}
	
	
}
