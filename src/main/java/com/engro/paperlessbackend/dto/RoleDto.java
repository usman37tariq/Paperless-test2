package com.engro.paperlessbackend.dto;

import java.util.List;

import com.engro.paperlessbackend.entities.Role;
import com.engro.paperlessbackend.entities.RoleResource;

public class RoleDto {
	
	private Role role;
	private List<RoleResource> resources;
	
	public RoleDto() {}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<RoleResource> getResources() {
		return resources;
	}

	public void setResources(List<RoleResource> resources) {
		this.resources = resources;
	}
}
