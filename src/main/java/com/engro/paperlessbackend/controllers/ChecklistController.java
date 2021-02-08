package com.engro.paperlessbackend.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.ChecklistDto;
import com.engro.paperlessbackend.dto.ChecklistTemplateAdditionDto;
import com.engro.paperlessbackend.entities.Checklist;
import com.engro.paperlessbackend.services.ChecklistService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class ChecklistController {
	
	@Autowired
	ChecklistService checklistService;
	
	@ApiOperation(value = "Add New Checklist", tags = "Checklist")
	@RequestMapping(method = RequestMethod.POST, value = "checklist")
	public Object addChecklist(@RequestBody Checklist checklist) {
		return checklistService.addChecklist(checklist);
	}
	
	@ApiOperation(value = "Add Asset Template to Checklist", tags = "Checklist")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/assettemplate")
	public Object addAssetTemplateToChecklist(@RequestBody ChecklistTemplateAdditionDto checklistTemplate) {
		return checklistService.addAssetTemplateToChecklist(checklistTemplate);
	}
	
	@ApiOperation(value = "Get All Checklists", tags = "Checklist")
	@RequestMapping(method = RequestMethod.GET, value = "checklist")
	public Object getAllChecklists(HttpServletRequest request) {
		String addr= request.getRemoteAddr().toString();
		return checklistService.getAllChecklists();
	}
	
	@ApiOperation(value = "Get Checklist Details + Structure By Checklist ID", tags = "Checklist")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/structure/{checklistId}")
	public Object getChecklistDetailsByChecklistId(@PathVariable String checklistId) {
		return checklistService.getChecklistDetailsByChecklistId(Integer.valueOf(checklistId));
	}
	
	@ApiOperation(value = "Update Checklist Scheduling", tags = "Checklist")
	@RequestMapping(method = RequestMethod.PUT, value = "checklist/scheduling")
	public Object updateChecklistScheduling(@RequestBody ChecklistDto checklist) {
		return checklistService.updateChecklistScheduling(checklist);
	}
	/*This end-point is called to change details of checklist like name, department, */
	@ApiOperation(value = "Update Checklist", tags = "Checklist")
	@RequestMapping(method = RequestMethod.PUT, value = "checklist")
	public Object updateChecklist(@RequestBody Checklist checklist) {
		return checklistService.updateChecklist(checklist);
	}
	
	@ApiOperation(value = "Delete Checklist Structure Field by Field ID", tags = "Checklist")
	@RequestMapping(method = RequestMethod.DELETE, value = "checklist/structure/{fieldId}")
	public Object deleteChecklistStructureFieldByFieldId(@PathVariable String fieldId) {
		return checklistService.deleteChecklistStructureFieldByFieldId(Integer.valueOf(fieldId));
	}
	
	@ApiOperation(value = "Delete Checklist by Checklist ID", tags = "Checklist")
	@RequestMapping(method = RequestMethod.DELETE, value = "checklist/{checklistId}")
	public Object deleteChecklistByChecklistId(@PathVariable String checklistId) {
		return checklistService.deleteChecklistByChecklistId(Integer.valueOf(checklistId));
	}
	
	@ApiOperation(value = "Activate Checklist By Checklist ID", tags = "Checklist")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/activate/{checklistId}")
	public Object activateChecklistByChecklistId(@PathVariable Integer checklistId, @RequestHeader("userid") String userId) {
		return checklistService.activateChecklistByChecklistId(checklistId, userId);
	}
	
	@ApiOperation(value = "De-Activate Checklist By Checklist ID", tags = "Checklist")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/deactivate/{checklistId}")
	public Object DeActivateChecklistByChecklistId(@PathVariable String checklistId) {
		return checklistService.DeActivateChecklistByChecklistId(Integer.valueOf(checklistId));
	}
}
