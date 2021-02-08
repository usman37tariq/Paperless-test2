package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.entities.Section;
import com.engro.paperlessbackend.services.SectionService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class SectionController {

	@Autowired
	SectionService sectionService;

	@ApiOperation(value = "Get Sections List by Department ID", tags = "Section")
	@RequestMapping(method = RequestMethod.GET, value = "department/sections/{departmentId}")
	public Object getSectionsListByDepartmentId(@PathVariable String departmentId) {
		return sectionService.getSectionsListByDepartmentId(Integer.valueOf(departmentId));
	}

	@ApiOperation(value = "Get Sections List", tags = "Section")
	@RequestMapping(method = RequestMethod.GET, value = "section")
	public Object getSectionsList() {
		return sectionService.getSectionsList();
	}

	@ApiOperation(value = "Add Section", tags = "Section")
	@RequestMapping(method = RequestMethod.POST, value = "section")
	public Object addSection(@RequestBody Section section) {
		return sectionService.addSection(section);
	}

	@ApiOperation(value = "Update Section", tags = "Section")
	@RequestMapping(method = RequestMethod.PUT, value = "section")
	public Object updateSection(@RequestBody Section section) {
		return sectionService.updateSection(section);
	}

	@ApiOperation(value = "Delete Section By Section Id", tags = "Section")
	@RequestMapping(method = RequestMethod.DELETE, value = "section/{sectionId}")
	public Object deleteSectionBySectionId(@PathVariable String sectionId) {
		return sectionService.deleteSectionBySectionId(Integer.valueOf(sectionId));
	}
}
