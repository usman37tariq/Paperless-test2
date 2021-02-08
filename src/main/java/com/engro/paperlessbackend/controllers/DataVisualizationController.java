package com.engro.paperlessbackend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.services.DataVisualizationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class DataVisualizationController {

	@Autowired
	DataVisualizationService dataVisualizationService;

	@ApiOperation(value = "Get Hierarchy for Data Visualization", tags = "Data Visualization")
	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/hierarchy")
	public Object getHierarchyForDataVisualization() {
		return dataVisualizationService.getHierarchyForDataVisualization();
	}

	@ApiOperation(value = "Get Tag Data for Data Visualization", tags = "Data Visualization")
	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/getTagTrends/{tagName}/{startDate}/{endDate}")
	public Object getTagTrends(@PathVariable("tagName") String tagName , @PathVariable("startDate")  String startDate,
			@PathVariable("endDate")  String endDate) {
		return dataVisualizationService.getTagsData(tagName, startDate, endDate);
	}
	
//	@ApiOperation(value = "Get Checklist Data for Data Visualization", tags = "Data Visualization")
//	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/getChecklistData/{checklistId}/{startDate}/{endDate}")
//	public Object getChecklistData(@PathVariable("checklistId") Integer checklistId , @PathVariable("startDate")  String startDate,
//			@PathVariable("endDate")  String endDate) {
//		return dataVisualizationService.getChecklistData(checklistId, startDate, endDate);
//	}
	
//	@ApiOperation(value = "Get Checklist Data for Data Visualization", tags = "Data Visualization")
//	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/getChecklistData/{checklistId}/{date}/{direction}")
//	public Object getChecklistDataForADate(@PathVariable("checklistId") Integer checklistId , @PathVariable("date")  String date,
//			@PathVariable("direction")  Integer searchDirection) {
//		
//		return dataVisualizationService.getChecklistData(checklistId, date, searchDirection);
//	}
	
	@ApiOperation(value = "Get Checklist Data for Data Visualization", tags = "Data Visualization")
	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/getChecklistData/{checklistId}/{timestamp}/{direction}/{scheduleId}")
	public Object getChecklistDataForADate(@PathVariable("checklistId") Integer checklistId , @PathVariable("timestamp")  long timestamp,
			@PathVariable("direction")  Integer searchDirection, @PathVariable(name="scheduleId")  String scheduleId  )  {
		Integer schId ;
		if(scheduleId.isEmpty() || scheduleId.equalsIgnoreCase(("null"))) {
				schId = null;
		}
		else {
			schId = Integer.parseInt(scheduleId);
		}
		return dataVisualizationService.getChecklistData(checklistId, timestamp, searchDirection, schId);
	}
	
	@ApiOperation(value = "Get Checklist Schedule Data for Data Visualization", tags = "Data Visualization")
	@RequestMapping(method = RequestMethod.GET, value = "datavisualization/getChecklistData/{checklistScheduleId}")
	public Object getChecklistDataByScheduleId(@PathVariable("checklistScheduleId") Integer checklistScheduleId ) {
		
		return dataVisualizationService.getChecklistDataByScheduleId(checklistScheduleId);
	}
	
}
