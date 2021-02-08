package com.engro.paperlessbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.engro.paperlessbackend.dto.GroupDto;
import com.engro.paperlessbackend.entities.Group;
import com.engro.paperlessbackend.services.GroupService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/")
public class GroupController {
	
	@Autowired
	GroupService groupService;
	
	@ApiOperation(value = "Add New Group", tags = "Group")
	@RequestMapping(method = RequestMethod.POST, value = "group")
	public Object addNewGroup(@RequestBody GroupDto group) {
		return groupService.addNewGroup(group);
	}
	
	@ApiOperation(value = "Get All Groups", tags = "Group")
	@RequestMapping(method = RequestMethod.GET, value = "group")
	public Object getAllGroups() {
		return groupService.getAllGroups();
	}
	
	@ApiOperation(value = "Get Group Details (Resources/Modules + Users) By Group ID", tags = "Group")
	@RequestMapping(method = RequestMethod.GET, value = "group/details/{groupId}")
	public Object getGroupDetailsByGroupId(@PathVariable String groupId) {
		return groupService.getGroupDetailsByGroupId(Integer.valueOf(groupId));
	}
	
	@ApiOperation(value = "Update Group", tags = "Group")
	@RequestMapping(method = RequestMethod.PUT, value = "group")
	public Object updateGroup(@RequestBody Group group) {
		return groupService.updateGroup(group);
	}
	
	@ApiOperation(value = "Update Group Details (Resources/Modules + Users)", tags = "Group")
	@RequestMapping(method = RequestMethod.PUT, value = "group/details")
	public Object updateGroup(@RequestBody GroupDto group) {
		return groupService.updateGroupDetails(group);
	}
	
	@ApiOperation(value = "Get Users by Group Id", tags = "Group")
	@RequestMapping(method = RequestMethod.GET, value = "group/users/{groupId}")
	public Object getUsersByGroupId(@PathVariable String groupId) {
		return groupService.getUsersByGroupId(groupId);
	}
	
//	@ApiOperation(value = "Get Group Permissions By Id", tags = "Group")
//	@RequestMapping(method = RequestMethod.GET, value = "group/{groupId}")
//	public Object getGroupPermissionsById(@PathVariable String groupId) {
//		return groupService.getGroupPermissionsById(groupId);
//	}
	
	@ApiOperation(value = "Delete Group by Group ID", tags = "Group")
	@RequestMapping(method = RequestMethod.DELETE, value = "group/{groupId}")
	public Object deleteGroupByGroupId(@PathVariable String groupId) {
		return groupService.deleteGroupByGroupId(Integer.valueOf(groupId));
	}
}
