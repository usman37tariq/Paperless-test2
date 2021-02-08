package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.entities.TemplateStructure;
import com.engro.paperlessbackend.services.TemplateStructureService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class TemplateStructureController {

	@Autowired
	TemplateStructureService templateStructureService;
	
	@ApiOperation(value = "Add Template Structure Field", tags = "Template")
	@RequestMapping(method = RequestMethod.POST, value = "template/structure")
	public Object saveTemplateStructure(@RequestBody TemplateStructure templateStructure) {
		return templateStructureService.addTemplateStructureField(templateStructure);
	}
	
	@ApiOperation(value = "Update Template Structure Field", tags = "Template")
	@RequestMapping(method = RequestMethod.PUT, value = "template/structure")
	public Object updateTemplateStructure(@RequestBody TemplateStructure templateStructure) {
		return templateStructureService.updateTemplateStructureField(templateStructure);
	}
	
	@ApiOperation(value = "Delete Template Structure Field by Field ID", tags = "Template")
	@RequestMapping(method = RequestMethod.DELETE, value = "template/structure/{fieldId}")
	public Object deleteTemplateStructureFieldByFieldId(@PathVariable String fieldId) {
		return templateStructureService.deleteTemplateStructureFieldByFieldId(Integer.valueOf(fieldId));
	}
	
	@ApiOperation(value = "Get Template Structrue by Template ID", tags = "Template")
	@RequestMapping(method = RequestMethod.GET, value = "template/structure/{templateId}")
	public Object getTemplateStructureByTemplateId(@PathVariable String templateId) {
		return templateStructureService.getTemplateStructureById(Integer.valueOf(templateId));
	}
}
