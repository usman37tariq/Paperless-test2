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
@Table(name = "checklist_state_field")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistStateField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_state_field_id")
	long checklistStateFieldId;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_state_id_fk")
	ChecklistState checklistState;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "template_field_id_fk")
	TemplateStructure templateStructure;

	@Column(name = "checklist_state_field_order_number")
	int checklistStateFieldOrderId;

	public ChecklistStateField() {
	}

	public ChecklistState getChecklistState() {
		return checklistState;
	}

	public void setChecklistState(ChecklistState checklistState) {
		this.checklistState = checklistState;
	}

	public long getChecklistStateFieldId() {
		return checklistStateFieldId;
	}

	public void setChecklistStateFieldId(long checklistStateFieldId) {
		this.checklistStateFieldId = checklistStateFieldId;
	}

	public TemplateStructure getTemplateStructure() {
		return templateStructure;
	}

	public void setTemplateStructure(TemplateStructure templateStructure) {
		this.templateStructure = templateStructure;
	}

	public int getChecklistStateFieldOrderId() {
		return checklistStateFieldOrderId;
	}

	public void setChecklistStateFieldOrderId(int checklistStateFieldOrderId) {
		this.checklistStateFieldOrderId = checklistStateFieldOrderId;
	}
}
