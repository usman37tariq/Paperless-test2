package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.services.ResourceService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class ResourceController {

	@Autowired
	ResourceService resourceService;
	
	@ApiOperation(value = "Get All Resources", tags = "Resource")
	@RequestMapping(method = RequestMethod.GET, value = "resource")
	public Object getAllResources() {
		return resourceService.getAllResources();
	}
}
