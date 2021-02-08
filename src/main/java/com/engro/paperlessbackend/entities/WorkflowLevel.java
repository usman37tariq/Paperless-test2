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

@Entity
@Table(name = "tbl_workflow_level")
@EntityListeners(AuditingEntityListener.class)
public class WorkflowLevel {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "workflow_level_id")
	private int workflowLevelId;

	@NotNull
	@Column(name = "workflow_level_name")
	private String workflowLevelName;

	public Integer getWorkflowLevelId() {
		return workflowLevelId;
	}

	public void setWorkflowLevelId(int workflowLevelId) {
		this.workflowLevelId = workflowLevelId;
	}

	public String getWorkflowLevelName() {
		return workflowLevelName;
	}

	public void setWorkflowLevelName(String workflowLevelName) {
		this.workflowLevelName = workflowLevelName;
	}

}
