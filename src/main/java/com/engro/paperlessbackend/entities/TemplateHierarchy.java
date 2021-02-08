package com.engro.paperlessbackend.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.engro.paperlessbackend.entityid.TemplateHierarchyId;

@Entity
@Table(name = "tbl_template_hierarchy")
@IdClass(TemplateHierarchyId.class)
@EntityListeners(AuditingEntityListener.class)
public class TemplateHierarchy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "template_id_fk")
	private int templateId;
	
	@Id
	@NotNull
	@Column(name = "node_id_fk")
	private int nodeId;
	
	public TemplateHierarchy(){}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
}
