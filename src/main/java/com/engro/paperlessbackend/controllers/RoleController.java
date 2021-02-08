package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.RoleDto;
import com.engro.paperlessbackend.services.RoleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@ApiOperation(value = "Get Roles List", tags = "Role")
	@RequestMapping(method = RequestMethod.GET, value = "role")
	public Object getRolesList() {
		return roleService.getRolesList();
	}
	
	@ApiOperation(value = "Add New Role", tags = "Role")
	@RequestMapping(method = RequestMethod.POST, value = "role")
	public Object addNewRole(@RequestBody RoleDto role) {
		return roleService.addNewRole(role);
	}
	
	@ApiOperation(value = "Update Role", tags = "Role")
	@RequestMapping(method = RequestMethod.PUT, value = "role")
	public Object updateRole(@RequestBody RoleDto role) {
		return roleService.updateRole(role);
	}
	
	@ApiOperation(value = "Delete Role", tags = "Role")
	@RequestMapping(method = RequestMethod.DELETE, value = "role/{roleId}")
	public Object deleteRoleByRoleId(@PathVariable String roleId) {
		return roleService.deleteRoleByRoleId(Long.valueOf(roleId));
	}
	
	@ApiOperation(value = "Get Role By Role ID", tags = "Role")
	@RequestMapping(method = RequestMethod.GET, value = "role/{roleId}")
	public Object getRoleByRoleId(@PathVariable String roleId) {
		return roleService.getRoleByRoleId(Long.valueOf(roleId));
	}
}
