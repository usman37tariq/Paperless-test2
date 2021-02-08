package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.entities.Template;
import com.engro.paperlessbackend.services.TemplateService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class TemplateController {
	
	@Autowired
	TemplateService templateService;
	
	@ApiOperation(value = "Add Template", tags = "Template")
	@RequestMapping(method = RequestMethod.POST, value = "template")
	public Object addTemplate(@RequestBody Template template) {
		return templateService.addTemplate(template);
	}
	
	@ApiOperation(value = "Get All Templates", tags = "Template")
	@RequestMapping(method = RequestMethod.GET, value = "template")
	public Object getAllTemplates() {
		return templateService.getAllTemplates();
	}
	
	@ApiOperation(value = "Get All Asset Templates", tags = "Asset Template")
	@RequestMapping(method = RequestMethod.GET, value = "template/assettemplates")
	public Object getAllAssetTemplates() {
		return templateService.getAllAssetTemplates();
	}
	
	@ApiOperation(value = "Get Template By ID", tags = "Template")
	@RequestMapping(method = RequestMethod.GET, value = "template/{templateId}")
	public Object getTemplateById(@PathVariable String templateId) {
		return templateService.getTemplateById(Integer.valueOf(templateId));
	}
	
	@ApiOperation(value = "Update Template", tags = "Template")
	@RequestMapping(method = RequestMethod.PUT, value = "template")
	public Object updateTemplate(@RequestBody Template template) {
		return templateService.updateTemplate(template);
	}
	
	@ApiOperation(value = "Delete Template by ID", tags = "Template")
	@RequestMapping(method = RequestMethod.DELETE, value = "template/{templateId}")
	public Object deleteTemplateById(@PathVariable String templateId) {
		return templateService.deleteTemplateById(Integer.valueOf(templateId));
	}
}
