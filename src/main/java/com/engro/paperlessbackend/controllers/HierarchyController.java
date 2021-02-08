package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.entities.Hierarchy;
import com.engro.paperlessbackend.services.HierarchyService;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class HierarchyController {
	
	private static Logger logger = LoggerFactory.getLogger(HierarchyController.class);
	
	@Autowired
	HierarchyService hierarchyService;
	
	@ApiOperation(value = "Add Node to Hierarchy", tags = "Hierarchy")
	@RequestMapping(method = RequestMethod.POST, value = "hierarchy")
	public Object addNodeToHierarchy(@RequestBody Hierarchy node) {
		return hierarchyService.addNewNodeToHierarchy(node);
	}
	
	@ApiOperation(value = "Get Hierarchy", tags = "Hierarchy")
	@RequestMapping(method = RequestMethod.GET, value = "hierarchy")
	public Object getHierarchy() {
		logger.info("Going to get hierarchy");
		return hierarchyService.getHierarchy();
	}
	
	@ApiOperation(value = "Update Hierarchy Node", tags = "Hierarchy")
	@RequestMapping(method = RequestMethod.PUT, value = "hierarchy")
	public Object updateNodeInHierarchy(@RequestBody Hierarchy node) {
		return hierarchyService.updateNodeInHierarchy(node);
	}
	
	@ApiOperation(value = "Delete Hierarchy Node by ID", tags = "Hierarchy")
	@RequestMapping(method = RequestMethod.DELETE, value = "hierarchy/{nodeId}")
	public Object deleteNodeById(@PathVariable String nodeId) {
		return hierarchyService.deleteNodeFromHierarchy(Integer.valueOf(nodeId));
	}
	
	@ApiOperation(value = "Get All Hierarchy Nodes Having Templates (ASSET Nodes)", tags = "Hierarchy")
	@RequestMapping(method = RequestMethod.GET, value = "hierarchy/templatenodes")
	public Object getHierarchyAssetNodesHavingTemplate() {
		
		logger.info("Going to get all nodes hierarchy");
		return hierarchyService.getHierarchyAssetNodesHavingTemplate();
	}
}
