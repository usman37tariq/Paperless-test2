package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.entities.Department;
import com.engro.paperlessbackend.services.DepartmentService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@ApiOperation(value = "Get Departments List", tags = "Department")
	@RequestMapping(method = RequestMethod.GET, value = "department")
	public Object getDepartmentsList() {
		return departmentService.getDepartmentsList();
	}

	@ApiOperation(value = "Add Department", tags = "Department")
	@RequestMapping(method = RequestMethod.POST, value = "department")
	public Object addDepartment(@RequestBody Department department) {
		return departmentService.addDepartment(department);
	}

	@ApiOperation(value = "Update Department", tags = "Department")
	@RequestMapping(method = RequestMethod.PUT, value = "department")
	public Object updateDepartment(@RequestBody Department department) {
		return departmentService.updateDepartment(department);
	}

	@ApiOperation(value = "Delete Department By Department Id", tags = "Department")
	@RequestMapping(method = RequestMethod.DELETE, value = "department/{departmentId}")
	public Object deleteDepartmentByDepartmentId(@PathVariable String departmentId) {
		return departmentService.deleteDepartmentByDepartmentId(Integer.valueOf(departmentId));
	}
}
