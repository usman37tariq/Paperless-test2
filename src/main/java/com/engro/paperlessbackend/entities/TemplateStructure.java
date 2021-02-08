package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_template_structure")
@EntityListeners(AuditingEntityListener.class)
public class TemplateStructure {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "field_id")
	private int fieldId;
	
	@NotNull
	@Column(name = "template_id_fk")
	private int templateId;
	
	@NotNull
	@Column(name = "description")
	private String description;
	
	
	@NotNull
	@Column(name = "field_type")
	private String fieldType;
	
	@NotNull
	@Column(name = "order_id")
	private int orderId;
	
	@Column(name = "lower_limit")
	private Double lowerLimit;
	
	@Column(name = "upper_limit")
	private Double upperLimit;
	
	@Column(name = "uom")
	private String unitOfMeasure;
	
	public TemplateStructure(){}
	
	public int getFieldId() {
		return fieldId;
	}

	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	@JsonIgnore
	public boolean isText() {
		return this.fieldType.equals(Constants.TAG_TYPE_TEXT);
	}
}
