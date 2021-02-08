package com.engro.paperlessbackend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tbl_checklist_approver_group")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistApproverGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	int id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "group_id_fk")
	Group group;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_id_fk")
	Checklist checklist;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "workflow_level_id_fk")
	WorkflowLevel workflowLevel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public WorkflowLevel getWorkflowLevel() {
		return workflowLevel;
	}

	public void setWorkflowLevel(WorkflowLevel workflowLevel) {
		this.workflowLevel = workflowLevel;
	}

}
