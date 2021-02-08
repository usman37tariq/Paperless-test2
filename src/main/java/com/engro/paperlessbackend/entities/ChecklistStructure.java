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
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tbl_checklist_structure")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistStructure {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "checklist_field_id")
	private int checklistFieldId;

	// templateFieldId
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "template_field_id_fk")
	private TemplateStructure templateStructure;

	// @NotNull
	// @Column(name = "checklist_id_fk")
	// private int checklistId;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "checklist_id_fk")
	private Checklist checklistId;

	@Column(name = "checklist_field_order_number")
	private int checklistFieldOrderId;

	@Column(name = "updated_at", columnDefinition = "bigint default (extract(epoch from now())::bigint) * 1000", insertable = false)
	private Long updatedAt;

	public ChecklistStructure() {
	}

	public int getChecklistFieldId() {
		return checklistFieldId;
	}

	public void setChecklistFieldId(int checklistFieldId) {
		this.checklistFieldId = checklistFieldId;
	}

	// public int getTemplateFieldId() {
	// return templateFieldId;
	// }

	// public void setTemplateFieldId(int templateFieldId) {
	// this.templateFieldId = templateFieldId;
	// }

	public TemplateStructure getTemplateStructure() {
		return templateStructure;
	}

	public void setTemplateStructure(TemplateStructure templateStructure) {
		this.templateStructure = templateStructure;
	}

	// public int getChecklistId() {
	// return checklistId;
	// }
	//
	// public void setChecklistId(int checklistId) {
	// this.checklistId = checklistId;
	// }

	public int getChecklistFieldOrderId() {
		return checklistFieldOrderId;
	}

	public Checklist getChecklist() {
		return checklistId;
	}

	public void setChecklist(Checklist checklist) {
		this.checklistId = checklist;
	}

	public void setChecklistFieldOrderId(int checklistFieldOrderId) {
		this.checklistFieldOrderId = checklistFieldOrderId;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

}
