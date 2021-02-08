package com.engro.paperlessbackend.entityid;

import java.io.Serializable;

import com.engro.paperlessbackend.entities.Resource;
import com.engro.paperlessbackend.entities.Role;

public class RoleResourceId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long role;
	private int resource;
	
	public RoleResourceId() {}
	
	public RoleResourceId(Role role, Resource resource) {
        this.role = role.getRoleId();
        this.resource = resource.getResourceId();
    }
}
