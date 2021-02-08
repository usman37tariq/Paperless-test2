package com.engro.paperlessbackend.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tag_detail")
@EntityListeners(AuditingEntityListener.class)
public class TagDetail {

	@Id
	@NotNull
	@Column(name = "tag_id")
	private String tagId;

	@Column(name = "description")
	private String description;

	@Column(name = "tag_opc_name")
	private String tagOpcName;

	@Column(name = "tag_type")
	private String tagType;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "data_source")
	private String dataSource;

	@Column(name = "scheduled")
	private boolean scheduled;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "organization_id")
	private Organization organizationId;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "tag_name")
	private String tagName;

	@Column(name = "execution_interval")
	private Integer executionInterval;

	@Column(name = "last_execution_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Timestamp lastExecutionDate;

	@Column(name = "tag_name_datasource")
	private String tagNameDatasource;

	@Column(name = "tag_data_type")
	private String tagDataType;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "template_field_id")
	private TemplateStructure templateField;
	
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTagOpcName() {
		return tagOpcName;
	}

	public void setTagOpcName(String tagOpcName) {
		this.tagOpcName = tagOpcName;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public Organization getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Organization organizationId) {
		this.organizationId = organizationId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getExecutionInterval() {
		return executionInterval;
	}

	public void setExecutionInterval(Integer executionInterval) {
		this.executionInterval = executionInterval;
	}

	public Timestamp getLastExecutionDate() {
		return lastExecutionDate;
	}

	public void setLastExecutionDate(Timestamp lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}

	public String getTagNameDatasource() {
		return tagNameDatasource;
	}

	public void setTagNameDatasource(String tagNameDatasource) {
		this.tagNameDatasource = tagNameDatasource;
	}

	public String getTagDataType() {
		return tagDataType;
	}

	public void setTagDataType(String tagDataType) {
		this.tagDataType = tagDataType;
	}

	
	public TemplateStructure getTemplateField() {
		return templateField;
	}

	public void setTemplateField(TemplateStructure templateField) {
		this.templateField = templateField;
	}
	
	

}
