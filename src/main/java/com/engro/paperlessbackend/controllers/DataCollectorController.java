package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.ChecklistApproveDto;
import com.engro.paperlessbackend.dto.ChecklistDataEntryDto;
import com.engro.paperlessbackend.entities.ChecklistStatusHistory;
import com.engro.paperlessbackend.services.DataCollectorService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class DataCollectorController {

	@Autowired
	DataCollectorService dataCollectorService;
	
	@ApiOperation(value = "Get All Notification Counts", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/notification/count/{userId}")
	public Object getAllNotificationCounts(@PathVariable String userId) {
		return dataCollectorService.getAllNotificationCounts(userId);
	}
	
	@ApiOperation(value = "Get All Checklists Assigned to User", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/data/{userId}")
	public Object getAllChecklistsAssignedToUser(@PathVariable String userId) {
		return dataCollectorService.getAllChecklistsAssignedToUser(userId);
	}
	
	@ApiOperation(value = "Claim Checklist By Checklist Schedule ID", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/claim/{checklistScheduleId}/{userId}")
	public Object claimChecklistByChecklistScheduleId(@PathVariable String checklistScheduleId, @PathVariable String userId) {
		return dataCollectorService.claimChecklistByChecklistScheduleId(Integer.valueOf(checklistScheduleId), userId);
	}
	
	@ApiOperation(value = "Skip Checklist", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/skip")
	public Object skipChecklist(@RequestBody ChecklistStatusHistory checklistStatusHistory) {
		return dataCollectorService.skipChecklist(checklistStatusHistory);
	}
	
	@ApiOperation(value = "Submit Checklist after Data Entry", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/submit")
	public Object submitChecklistData(@RequestBody ChecklistDataEntryDto checklistData) {
		return dataCollectorService.submitChecklistData(checklistData);
	}
	
	@ApiOperation(value = "Get All Checklists Assigned to Approver User", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.GET, value = "checklist/data/approver/{userId}")
	public Object getAllChecklistsAssignedToApproverUser(@PathVariable String userId) {
		return dataCollectorService.getAllChecklistsAssignedToApproverUser(userId);
	}
	
	@ApiOperation(value = "Approve Checklist", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/approve")
	public Object approveChecklist(@RequestBody ChecklistApproveDto checklistApprove) {
		return dataCollectorService.approveChecklist(checklistApprove);
	}
	
	@ApiOperation(value = "Save Checklist", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.POST, value = "checklist/save")
	public Object saveChecklistData(@RequestBody ChecklistDataEntryDto checklistData) {
		
		return dataCollectorService.saveChecklistData(checklistData);
	}
	
	@ApiOperation(value = "Get Checklist Data by Schedule ID", tags = "Data Collector")
	@RequestMapping(method = RequestMethod.GET, value = "datacollector/getChecklistData/{checklistScheduleId}")
	public Object saveChecklistData(@PathVariable Integer checklistScheduleId) {
		
		return dataCollectorService.getChecklistScheduleDataByChecklistScheduleId(checklistScheduleId);
	}
	
}
