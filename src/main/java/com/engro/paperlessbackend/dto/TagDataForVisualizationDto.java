package com.engro.paperlessbackend.dto;

import java.util.List;

/**
 * Tag DTO for Visualization
 * 
 * @author Ehsan Waris
 *
 */
public class TagDataForVisualizationDto {
	List<String> columns;
	Object[][] data;
	String unitOfMeasure;
	String tagName;
	String tagType;

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

}
