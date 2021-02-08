package com.engro.paperlessbackend.entityid;

import java.io.Serializable;

public class TemplateHierarchyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int templateId;
	private int nodeId;
	
	TemplateHierarchyId() {}
	
	public TemplateHierarchyId(int templateId, int nodeId) {
        this.templateId = templateId;
        this.nodeId = nodeId;
    }
}
